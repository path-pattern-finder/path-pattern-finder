# path-pattern-finder



## Description



**path-pattern finder** is a Java library (and basic command line app) to find patterns in a list of strings or paths, using a particular set of rules.



It is useful when you wish to translate a set of file-paths into their constant and non-constant (variable) components.



e.g. consider some paths

```

somedir/somefilename_001.txt

somedir/somefilename_002.txt

somedir/somefilename_003.txt

```



In this case, the library would find the pattern:

```

somedir/somefilename_{0}.txt

```

where {0} indexes each component.



## Example



For example, this is the output from a directory (with several nested sub-directories) of pictures from a holiday in Italy.



Files take the form:

```

C:\Users\Owen\Pictures\Holidays\Italy at Easter (March 2013)\Milan\2013-04-02 20.07.42.jpg

C:\Users\Owen\Pictures\Holidays\Italy at Easter (March 2013)\Milan\IMG_4677.JPG

C:\Users\Owen\Pictures\Holidays\Italy at Easter (March 2013)\Milan\IMG_4678.JPG

C:\Users\Owen\Pictures\Holidays\Italy at Easter (March 2013)\Milan\IMG_4682.JPG

...

C:\Users\Owen\Pictures\Holidays\Italy at Easter (March 2013)\Verona\IMG_4252.JPG

C:\Users\Owen\Pictures\Holidays\Italy at Easter (March 2013)\Verona\IMG_4255.JPG

C:\Users\Owen\Pictures\Holidays\Italy at Easter (March 2013)\Verona\IMG_4261.JPG

etc.

```



The command-line app would produce:

```

There are 274 input paths in total

Pattern is: C:\Users\Owen\Pictures\Archived\Holidays\Italy at Easter (March 2013)\${0}\${1}${2}.jpg

${0} = 6 unique strings e.g. "Venice" (145), "Milan" (38), "Verona" (34)

${1} = "IMG_" (273) | "2013-04-02 20.07." (1)

${2} = 274 unique integers between 42 and 4766 inclusive

```



## How to use?

Either as a Java library, or a command-line tool.

Being pure Java, it works on Windows, Linux, Mac and several other operating systems.



### As a library



Call an appropriate static method in ```com.owenfeehan.pathpatternfinder.PathPatternFinder```  (e.g. ```findPatternPath``` or ```findPatternStr```)



See the Javadocs for more detailed code documentation.



### As a command-line app



Call ```PathPatternFinder``` as an application with a single wildcard argument.



This will

1. recursively search the current working directory for files matching the wildcard argument (a glob)

2. find the pattern

3. print the pattern to the console



## Applications



* Images from microscopes often come in a sequential manner, with the sequence encoded in the file-path. This library helps find the sequence. 

* It is similarly used by the Anchor image analysis software suite.



## Author



[Owen Feehan](http://www.owenfeehan.com) distributed under MIT license.

