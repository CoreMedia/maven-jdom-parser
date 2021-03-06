<!--
  Copyright 2018 CoreMedia AG, Hamburg

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  -->

# Maven JDom Parser

Enables reading and writing of Maven POM files without losing formats and comments.

It is based on work by [Robert Scholte](https://github.com/rfscholte) for version 3.0 of the
[Maven Release Plugin](https://github.com/apache/maven-release/).

## Installation

The project/library is (currently) not available in _Maven Central_, hence it must be cloned, built, installed and
deployed manually.

## Usage

This project used [ETL][etl]. The typical usage is:

1. Instantiate the _JDomModelETL_ (probably using the _JDomModelETLFactory_).
2. Read the _pom.xml_ file using the _extract_ method.
3. Get the model object using the using the _getModel_ method and do the desired modifications on it.
   **NOTE** that the _transform_ method is not implemented.
4. Write the (modified) _pom.xml_ file using the _load_ method.

## License
This code is under the [Apache Licence v2][license]


[etl]: https://en.wikipedia.org/wiki/Extract%2C_transform%2C_load
[license]: https://www.apache.org/licenses/LICENSE-2.0
