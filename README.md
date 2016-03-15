![Parallax 3D library](https://github.com/thothbot/parallax/wiki/images/logo.png)

[![Build Status](https://travis-ci.org/thothbot/parallax.svg?branch=master)](https://travis-ci.org/thothbot/parallax)

![Demo](http://thothbot.github.com/parallax/static/examples_banner.jpg)

### Cross-platform Java 3D SDK

[Wiki](https://github.com/thothbot/parallax/wiki)
| [Bugs](https://github.com/thothbot/parallax/issues)

**Current status of** `Parallax 2.0` is *beta*

Parallax 2.0 is completely new Java 3D SDK. We wanted to make it as simple as possible.
If you know how to use [three.js](http://github.com/mrdoob/three.js) you can start quick, because Parallax implements almost all threejs API. But also you have a set of tools and libraries to write cross-platform 3D applications.

Parallax SDK allows you to write 3D application for the following platforms:
* Browsers (GWT) - *ready*
* Android - *in the pipeline, should be ready in Parallax 2.0*
* Desktop Window, Linus, OS X - *future*

#### Quick start

We prepared Parallax SDK application template.
Go to the [parallax-application-template](https://github.com/thothbot/parallax-application-template) project and just follow the manual to make your first Parallax application.

Parallax 2.0 SDK will be in the [maven central repository](http://search.maven.org), but snapshots are located in the [sonatype repo](https://oss.sonatype.org/content/repositories/snapshots/):

```xml
<!-- pom.xml example -->
<repositories>
    <repository>
        <id>oss-sonatype</id>
        <name>oss-sonatype</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

Parallax SDK contains the following main packages:
* **parallax** - Parallax SDK core. Should be included to all applications
* **parallax-gwt** - Parallax for <a href="https://developers.google.com/web-toolkit/">Google Web Toolkit</a> platform, to run your 3D applications in any modern browsers.
* **parallax-android** - Parallax for Android platform.
* **parallax-controllers** - Cross-platform *Extension* to use mouse, trackball etc
* **parallax-loaders** - Cross-platform *Extension* to load models, fonts etc
* **parallax-renderer-plugins** - Cross-platform *Extension* for post-processing rendering etc
* **parallax-renderer-css-gwt** - GWT platform *Extension* for rendering using CSS.
* **parallax-renderer-raytracing-gwt** - GWT platform *Extension* for experiments in raytracing rendering

To launch Parallax 2.0 SDK GWT tests run:

```
gradle :tests:parallax-tests-gwt:superDev
```

[Change log](https://github.com/thothbot/parallax/releases)
| [1.x Download](http://github.com/thothbot/parallax/wiki/Download)
| [1.x API Reference](http://thothbot.github.com/parallax/docs/index.html)
| [1.x Demo](http://thothbot.github.com/parallax/demo/index.html)
