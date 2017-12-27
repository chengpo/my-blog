From time to time, I was wondering how to clone a stream object.<br>
But it appears stream object is designed as 1 time use item, it does not support copying.
So, I ends up with duplicate the code for generating the stream.<br>
Something like:

``` java
final long totalPapers = postIdRepository.getPostIdsByTag(tag).count();
final List<Paper> papers = postIdRepository.getPostIdsByTag(tag)
                                            /* some intermediate operations */
                                            .collect(Collectors.toList());
```

Well, it works. Just works in an ugly way though. <br>

Eventually, I realize instead of copy-paste code for generating the same stream object.
I can decouple the logic for generating stream with the Supplier function interface.

Here we go:

``` java
final Supplier<Stream<Paper.Id>> postIds = () -> postIdRepository.getPostIdsByTag(tag);

final long totalPapers = postIds.get().count();

final List<Paper> papers = postIds.get()
                                  /* some intermediate operations */
                                  .collect(Collectors.toList());
``` 

That's it. No more duplicate code.
<!--eof-->