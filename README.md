![maven workflow](https://github.com/chengpo/my-blog/actions/workflows/maven.yml/badge.svg)
# My Blog

Source of <a href="http://monkey-blogger.herokuapp.com" target="_blank">monkey-blogger.herokuapp.com</a>

## Technologies Behind

- Bootstrap for frontend page layout
- RequireJS for loading Javascript modules
- AngularJS for frontend web App
- HK2 for dependency injection
- Jersey for Restful API
- Mockito for mocking test objects
- Kotlin for backend service
- Embedded Tomcat for hosting the website
- Docker for running in container environment

## Run with Command Line

```

$ make clean
$ make build
$ make run

```

## Run docker image

```

$ make docker-build
$ make docker-run

```


## Start by Java 9
Java 9 by default does not include java.se aggregate module on class path.<br>
To make the Java EE APIs available at runtime, specify the following command-line options: 

```
--add-modules java.xml.bind --add-modules java.activation
```

## Update dependencies
```
mvn versions:display-dependency-updates
```

## License
    MIT License

	Copyright (c) 2017 - 2022 Po Cheng

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
	
