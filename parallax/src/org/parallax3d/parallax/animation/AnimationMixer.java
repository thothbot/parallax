/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
 *
 * This file is part of Parallax project.
 *
 * Parallax is free software: you can redistribute it and/or modify it
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 *
 * Parallax is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution
 * 3.0 Unported License. for more details.
 *
 * You should have received a copy of the the Creative Commons Attribution
 * 3.0 Unported License along with Parallax.
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */
package org.parallax3d.parallax.animation;

import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.math.Interpolant;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.EventDispatcher;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.undefined;

/**
 *
 * Player for AnimationClips.
 *
 *
 * @author Ben Houston / http://clara.io/
 * @author David Sarno / http://lighthaus.us/
 * @author tschw
 * @author thothbot
 */
@ThreejsObject("THREE.AnimationMixer")
public class AnimationMixer extends EventDispatcher {

    public class ClipActions {

        List<AnimationAction> knownActions = new ArrayList<>();
        FastMap<AnimationAction> actionByRoot = new FastMap<>();

    }

    GeometryObject _root;

    int _accuIndex = 0;

    double time = 0.0;

    double timeScale = 1.0;

    // private
    List<AnimationAction> _actions;
    int _nActiveActions;
    FastMap<ClipActions> _actionsByClip;

    List<PropertyBinding> _bindings;
    int _nActiveBindings;
    FastMap<FastMap<PropertyMixer>> _bindingsByRootAndName;

    List<Interpolant> _controlInterpolants;
    int _nActiveControlInterpolants;

    public AnimationMixer(GeometryObject root) {
        this._root = root;

        this._initMemoryManager();
    }

    public AnimationAction clipAction(AnimationClip clip ) {
        clipAction(clip, this._root );
    }

    /**
     * return an action for a clip optionally using a custom root target
     * object (this method allocates a lot of dynamic memory in case a
     * previously unknown clip/root combination is specified)
     */
    public AnimationAction clipAction(AnimationClip clip, GeometryObject optionalRoot ) {

        GeometryObject root = optionalRoot;
        String rootUuid = root.getUUID();
        String clipName = clip.name;
        AnimationClip clipObject = clip;

//        ClipActions actionsForClip = this._actionsByClip.get( clipName );

        AnimationAction prototypeAction = null;

        if ( this._actionsByClip.containsKey( clipName) )
        {
            ClipActions actionsForClip = this._actionsByClip.get( clipName );

            if ( actionsForClip.actionByRoot.containsKey( rootUuid ) ) {

                return actionsForClip.actionByRoot.get( rootUuid );

            }

            // we know the clip, so we don't have to parse all
            // the bindings again but can just copy
            prototypeAction = actionsForClip.knownActions.get(0);

            // also, take the clip from the prototype action
            clipObject = prototypeAction._clip;

            if ( clip != clipObject ) {

                throw new Error(  "Different clips with the same name detected!" );

            }

        }

        // clip must be known when specified via string
        if ( clipObject == null ) return null;

        // allocate all resources required to run it
        AnimationAction newAction = new AnimationAction( this, clipObject, optionalRoot );

        this._bindAction( newAction, prototypeAction );

        // and make the action known to the memory manager
        this._addInactiveAction( newAction, clipName, rootUuid );

        return newAction;

    }

    public void existingAction( AnimationClip clip ) {
        existingAction(clip, this._root );
    }

    /**
     * get an existing action
     * @param clip
     */
    public AnimationAction existingAction(AnimationClip clip, GeometryObject optionalRoot ) {

        GeometryObject root = optionalRoot;
        String rootUuid = root.getUUID();

        String clipName = clip.getName();

        if ( this._actionsByClip.containsKey(clipName) ) {

            return this._actionsByClip.get(clipName).getActionByRoot()[ rootUuid ] || null;

        }

        return null;

    }

    /**
     * deactivates all previously scheduled actions
     * @return
     */
    public AnimationMixer stopAllAction() {

        this._nActiveActions = 0;
        this._nActiveBindings = 0;

        for ( int i = 0; i != this._nActiveActions; ++ i ) {

            this._actions.get(i).reset();

        }

        for ( int i = 0; i != this._nActiveBindings; ++ i ) {

            this._bindings.get(i).useCount = 0;

        }

        return this;

    }

    // advance the time and update apply the animation
    public AnimationMixer update( double deltaTime ) {

        deltaTime *= this.timeScale;

        double time = this.time += deltaTime;
        int timeDirection = Mathematics.sign( deltaTime );

        int accuIndex = this._accuIndex ^= 1;

        // run active actions

        for ( int i = 0; i != this._nActiveActions; ++ i ) {

            AnimationAction action = this._actions.get(i);

            if ( action.enabled ) {

                action._update( time, deltaTime, timeDirection, accuIndex );

            }

        }

        // update scene graph

        for ( int i = 0; i != this._nActiveBindings; ++ i ) {

            this._bindings.get(i).apply( accuIndex );

        }

        return this;

    }

    /**
     * return this mixer's root target object
     * @return
     */
    public GeometryObject getRoot() {

        return this._root;

    }

    /**
     * free all resources specific to a particular clip
     * @param clip
     */
    public void uncacheClip( AnimationClip clip ) {

        List<AnimationAction> actions = this._actions;
        String clipName = clip.getName();

        if ( this._actionsByClip.containsKey(clipName))
        {

            ClipActions actionsForClip = this._actionsByClip.get( clipName );
            // note: just calling _removeInactiveAction would mess up the
            // iteration state and also require updating the state we can
            // just throw away

            List<AnimationAction> actionsToRemove = actionsForClip.knownActions;

            for ( int i = 0, n = actionsToRemove.size(); i != n; ++ i ) {

                AnimationAction action = actionsToRemove.get(i);

                this._deactivateAction( action );

                int cacheIndex = action._cacheIndex;
                AnimationAction lastInactiveAction = actions.get(actions.size() - 1);

                action._cacheIndex = -1;
                action._byClipCacheIndex = null;

                lastInactiveAction._cacheIndex = cacheIndex;
                actions.set(cacheIndex, lastInactiveAction);
                actions.remove(actions.size() - 1);

                this._removeInactiveBindingsForAction( action );

            }

            this._actionsByClip.remove( clipName );
        }

    }

    /**
     * free all resources specific to a particular root target object
     */
    public void uncacheRoot( GeometryObject root ) {

        String rootUuid = root.getUUID();
//                actionsByClip = this._actionsByClip;

        for ( String clipName : _actionsByClip.keySet() ) {

            FastMap<AnimationAction> actionByRoot = _actionsByClip.get( clipName ).actionByRoot,

            if ( actionByRoot.containsKey( rootUuid ) ) {

                this._deactivateAction( action );
                this._removeInactiveAction( action );

            }

        }

        FastMap<FastMap<PropertyMixer>> bindingsByRoot = this._bindingsByRootAndName;

        if ( bindingsByRoot.containsKey( rootUuid ) ) {

            FastMap<PropertyMixer> bindingByName = bindingsByRoot.get( rootUuid );

            for ( String trackName : bindingByName.keySet() ) {

                PropertyMixer binding = bindingByName.get( trackName );
                binding.restoreOriginalState();
                this._removeInactiveBinding( binding );

            }

        }

    }

    /**
     * remove a targeted clip from the cache
     * @param clip
     * @param optionalRoot
     */
    public void uncacheAction( AnimationClip clip, GeometryObject optionalRoot ) {

        AnimationAction action = this.existingAction( clip, optionalRoot );

        if ( action != null ) {

            this._deactivateAction( action );
            this._removeInactiveAction( action );

        }

    }

    private void _bindAction( AnimationAction action ) {
        _bindAction(action, null);
    }

    private void _bindAction(AnimationAction action, AnimationAction prototypeAction ) {

        GeometryObject root = action._localRoot;
        if(root == null) root = this._root;

        List<KeyframeTrack> tracks = action._clip.tracks;
        int nTracks = tracks.size();
        List<PropertyMixer> bindings = action._propertyBindings;
        List<Interpolant> interpolants = action._interpolants;

        String rootUuid = root.getUUID();
        FastMap<FastMap<PropertyMixer>> bindingsByRoot = this._bindingsByRootAndName;

        if ( ! bindingsByRoot.containsKey( rootUuid ) ) {

            bindingsByRoot.put( rootUuid, new FastMap<PropertyMixer>());

        }

        FastMap<PropertyMixer> bindingsByName = bindingsByRoot.get( rootUuid );

        for ( int i = 0; i != nTracks; ++ i ) {

            KeyframeTrack track = tracks.get(i);
            String trackName = track.name;

            if ( bindingsByName.containsKey( trackName ) ) {

                bindings.set(i, bindingsByName.get( trackName ));

            } else {

                PropertyMixer binding = bindings.get(i);

                if ( binding != null ) {

                    // existing binding, make sure the cache knows

                    if ( binding._cacheIndex == null ) {

                        ++ binding.referenceCount;
                        this._addInactiveBinding( binding, rootUuid, trackName );

                    }

                    continue;

                }

                AnimationAction path = prototypeAction != null ?
                    prototypeAction._propertyBindings.get(i).binding.parsedPath : null;

                binding = new PropertyMixer(
                        PropertyBinding.create( root, trackName, path ),
                        track.ValueTypeName, track.getValueSize() );

                ++ binding.referenceCount;
                this._addInactiveBinding( binding, rootUuid, trackName );

                bindings.set(i, binding);

            }

            interpolants.get(i).setResultBuffer(binding.buffer);

        }

    }

    public void _activateAction( AnimationAction action ) {

        if ( ! this._isActiveAction( action ) ) {

            if ( action._cacheIndex == -1 ) {

                // this action has been forgotten by the cache, but the user
                // appears to be still using it -> rebind

                String rootUuid = action._localRoot != null ? action._localRoot.getUUID() : _root.getUUID();
                String clipName = action._clip.name;
                ClipActions actionsForClip = this._actionsByClip.get( clipName );

                if(this._actionsByClip.containsKey( clipName ))
                    this._bindAction( action, actionsForClip.knownActions.get(0));
                else
                    this._bindAction( action );

                this._addInactiveAction( action, clipName, rootUuid );

            }

            List<PropertyMixer> bindings = action._propertyBindings;

            // increment reference counts / sort out state
            for ( int i = 0, n = bindings.size(); i != n; ++ i ) {

                PropertyMixer binding = bindings.get(i);

                if ( binding.useCount ++ == 0 ) {

                    this._lendBinding( binding );
                    binding.saveOriginalState();

                }

            }

            this._lendAction( action );

        }

    }

    // Memory manager

    private void _initMemoryManager() {

        this._actions = new ArrayList<>(); // 'nActiveActions' followed by inactive ones
        this._nActiveActions = 0;

        this._actionsByClip = new FastMap();
        // inside:
        // {
        // 		knownActions: Array< _Action >	- used as prototypes
        // 		actionByRoot: _Action			- lookup
        // }


        this._bindings = new ArrayList<>(); // 'nActiveBindings' followed by inactive ones
        this._nActiveBindings = 0;

        this._bindingsByRootAndName = new FastMap<>(); // inside: Map< name, PropertyMixer >


        this._controlInterpolants = new ArrayList<>(); // same game as above
        this._nActiveControlInterpolants = 0;

    }

    public boolean _isActiveAction( AnimationAction action ) {

        int index = action._cacheIndex;
        return index != -1 && index < this._nActiveActions;

    }

    private void _addInactiveAction(AnimationAction action, String clipName, String rootUuid ) {

        ClipActions actionsForClip;

        if ( ! this._actionsByClip.containsKey( clipName ) )
        {
            actionsForClip = new ClipActions();
            actionsForClip.knownActions = Arrays.asList(action);

            action._byClipCacheIndex = 0;

            this._actionsByClip.put( clipName , actionsForClip);

        } else {

            actionsForClip =  this._actionsByClip.get( clipName );

            List<AnimationAction> knownActions = actionsForClip.knownActions;

            action._byClipCacheIndex = knownActions.size();
            knownActions.add( action );

        }

        action._cacheIndex =  this._actions.size();
        this._actions.add( action );

        actionsForClip.actionByRoot.put( rootUuid , action );

    }

    private void _removeInactiveAction( AnimationAction action ) {

//        var actions = this._actions,
        AnimationAction lastInactiveAction = _actions.get(_actions.size() - 1);
        int cacheIndex = action._cacheIndex;

        lastInactiveAction._cacheIndex = cacheIndex;
        _actions.set(cacheIndex, lastInactiveAction);
        _actions.remove(_actions.size() - 1);

        action._cacheIndex = -1;


        String clipName = action._clip.name;
        FastMap<ClipActions> actionsByClip = this._actionsByClip;

        ClipActions actionsForClip = actionsByClip.get( clipName );
        List<AnimationAction> knownActionsForClip = actionsForClip.knownActions;

        AnimationAction lastKnownAction = knownActionsForClip.get(knownActionsForClip.size() - 1);

        int byClipCacheIndex = action._byClipCacheIndex;

        lastKnownAction._byClipCacheIndex = byClipCacheIndex;
        knownActionsForClip.set(byClipCacheIndex, lastKnownAction);
        knownActionsForClip.remove(knownActionsForClip.size() - 1);

        action._byClipCacheIndex = -1;


        FastMap<AnimationAction> actionByRoot = actionsForClip.actionByRoot;
        String rootUuid = ( actions._localRoot || this._root ).uuid;

        actionByRoot.remove( rootUuid );

        if ( knownActionsForClip.size() == 0 ) {

            actionsByClip.remove( clipName );

        }

        this._removeInactiveBindingsForAction( action );

    }

    private void _removeInactiveBindingsForAction( AnimationAction action ) {

        List<PropertyMixer> bindings = action._propertyBindings;
        for ( int i = 0, n = bindings.size(); i !== n; ++ i ) {

            PropertyMixer binding = bindings.get(i);

            if ( -- binding.referenceCount == 0 ) {

                this._removeInactiveBinding( binding );

            }

        }

    }

    private void _lendAction( AnimationAction action ) {

        // [ active actions |  inactive actions  ]
        // [  active actions >| inactive actions ]
        //                 s        a
        //                  <-swap->
        //                 a        s

//        var actions = this._actions,
        int prevIndex = action._cacheIndex,
            lastActiveIndex = this._nActiveActions ++;

        AnimationAction firstInactiveAction = _actions.get(lastActiveIndex);

        action._cacheIndex = lastActiveIndex;
        _actions.set(lastActiveIndex, action);

        firstInactiveAction._cacheIndex = prevIndex;
        _actions.set(prevIndex, firstInactiveAction);

    }

    private void _takeBackAction( AnimationAction action ) {

        // [  active actions  | inactive actions ]
        // [ active actions |< inactive actions  ]
        //        a        s
        //         <-swap->
        //        s        a

//        var actions = this._actions,
        int prevIndex = action._cacheIndex,
            firstInactiveIndex = -- this._nActiveActions;

        AnimationAction lastActiveAction = _actions.get(firstInactiveIndex);

        action._cacheIndex = firstInactiveIndex;
        _actions.set(firstInactiveIndex, action);

        lastActiveAction._cacheIndex = prevIndex;
        _actions.set(prevIndex, lastActiveAction);

    }

    // Memory management for PropertyMixer objects

    private void _addInactiveBinding( PropertyMixer binding, String rootUuid, String trackName ) {

        FastMap<FastMap<PropertyMixer>> bindingsByRoot = this._bindingsByRootAndName;

        if (  !bindingsByRoot.containsKey( rootUuid ) ) {

            bindingsByRoot.put( rootUuid, new FastMap<PropertyMixer>() );

        }

        FastMap<PropertyMixer> bindingByName = bindingsByRoot.get( rootUuid );

        bindingByName.put( trackName , binding );

        binding._cacheIndex = _bindings.size();
        _bindings.add( binding );

    }

    private void _removeInactiveBinding( binding ) {

        var bindings = this._bindings,
                propBinding = binding.binding,
                rootUuid = propBinding.rootNode.uuid,
                trackName = propBinding.path,
                bindingsByRoot = this._bindingsByRootAndName,
                bindingByName = bindingsByRoot[ rootUuid ],

                lastInactiveBinding = bindings[ bindings.length - 1 ],
                cacheIndex = binding._cacheIndex;

        lastInactiveBinding._cacheIndex = cacheIndex;
        bindings[ cacheIndex ] = lastInactiveBinding;
        bindings.pop();

        delete bindingByName[ trackName ];

        remove_empty_map: {

            for ( var _ in bindingByName ) break remove_empty_map;

            delete bindingsByRoot[ rootUuid ];

        }

    }

    private void _lendBinding( binding ) {

        var bindings = this._bindings,
                prevIndex = binding._cacheIndex,

                lastActiveIndex = this._nActiveBindings ++,

                firstInactiveBinding = bindings[ lastActiveIndex ];

        binding._cacheIndex = lastActiveIndex;
        bindings[ lastActiveIndex ] = binding;

        firstInactiveBinding._cacheIndex = prevIndex;
        bindings[ prevIndex ] = firstInactiveBinding;

    }

    private void _takeBackBinding( binding ) {

        var bindings = this._bindings,
                prevIndex = binding._cacheIndex,

                firstInactiveIndex = -- this._nActiveBindings,

                lastActiveBinding = bindings[ firstInactiveIndex ];

        binding._cacheIndex = firstInactiveIndex;
        bindings[ firstInactiveIndex ] = binding;

        lastActiveBinding._cacheIndex = prevIndex;
        bindings[ prevIndex ] = lastActiveBinding;

    }


    // Memory management of Interpolants for weight and time scale

    public Interpolant _lendControlInterpolant() {

        var interpolants = this._controlInterpolants,
                lastActiveIndex = this._nActiveControlInterpolants ++,
                interpolant = interpolants[ lastActiveIndex ];

        if ( interpolant === undefined ) {

            interpolant = new THREE.LinearInterpolant(
                    new Float32Array( 2 ), new Float32Array( 2 ),
                    1, this._controlInterpolantsResultBuffer );

            interpolant.__cacheIndex = lastActiveIndex;
            interpolants[ lastActiveIndex ] = interpolant;

        }

        return interpolant;

    }

    public void _takeBackControlInterpolant( Interpolant interpolant ) {

        var interpolants = this._controlInterpolants,
                prevIndex = interpolant.__cacheIndex,

                firstInactiveIndex = -- this._nActiveControlInterpolants,

                lastActiveInterpolant = interpolants[ firstInactiveIndex ];

        interpolant.__cacheIndex = firstInactiveIndex;
        interpolants[ firstInactiveIndex ] = interpolant;

        lastActiveInterpolant.__cacheIndex = prevIndex;
        interpolants[ prevIndex ] = lastActiveInterpolant;

    }
}
