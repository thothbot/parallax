__author__ = 'thothbot'

import sys

if sys.version_info < (2, 7):
    print("This script requires at least Python 2.7.")
    print("Please, update to a newer version: http://www.python.org/download/releases/")

import fnmatch
import os
import re
import collections
import urllib

MODULE = 'org.parallax3d.parallax'
STATIC_REWRITE = 'parallax.files.classpath'

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
CORE_DIR = os.path.join(SCRIPT_DIR, os.pardir, 'parallax', 'src')
OUTPUT_FILE = os.path.join(CORE_DIR, 'org', 'parallax3d', 'parallax.gwt.xml')

includes = ['*.java','*.fs','*.vs','*.glsl']
excludes = ['']

includes = r'|'.join([fnmatch.translate(x) for x in includes])
excludes = r'|'.join([fnmatch.translate(x) for x in excludes]) or r'$.'

listJava = []
listStatic = []

for root, _, filenames  in os.walk(CORE_DIR):
    filenames = [f for f in filenames if not re.match(excludes, f)]
    filenames = [f for f in filenames if re.match(includes, f)]

    for filename  in filenames:
        extension = os.path.splitext(filename)[1]
        if extension == '.java':
            relDir = os.path.relpath(root, os.path.join(CORE_DIR, 'org', 'parallax3d', 'parallax'))
            relFile = os.path.join(relDir, filename)
            listJava.append(relFile.replace("\\", "/").replace("./", ""))
        else:
            relDir = os.path.relpath(root, CORE_DIR)
            relFile = os.path.join(relDir, filename)
            listStatic.append(relFile.replace("\\", "/"))

        # filename = os.path.basename(filename)
        # print(root)

f = open(OUTPUT_FILE,'w')

f.write('<?xml version="1.0" encoding="UTF-8"?>\n<module rename-to="' + MODULE + '">\n')

f.write('\t<source path="parallax">\n')
for val in listJava:
    f.write('\t\t<include name="' + val + '" />\n')
f.write('\t</source>\n\n')

if len(listStatic):
    f.write('\t<define-configuration-property name="' + STATIC_REWRITE +'" is-multi-valued="true" />\n')
    for val in listStatic:
        f.write('\t<extend-configuration-property name="' + STATIC_REWRITE +'" value="' + val + '" />\n')

f.write('</module>')

f.close()
