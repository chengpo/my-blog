In the past few days, I refactored the backend code of this website. I prefer self explanatory code without "boilerplate".<br>
I also want the website can be directly runs outside of IDE which enable me to write and preview post from my Linux netbook more comfortable.<br>

Here are the lessons I learned from this code refactoring:<br>
### Prefer optional over checking null pointer
"Optional" is one of my favorite Java8 feature.<br>
"Optional" postpones null pointer checking to the very end of the data processing flow. The code has no more worry to null pointer exception while processing data.
I fell like all null pointer checking should be replaced by optional.

A simplified processing flow of loading post is as:

``` java
Optional.ofNullable(readTextFile(path))
        .map(
          (content) -> {
                return parseMarkdown(content);
          })
        .map(Optional::get)
        .orElseThrow(() -> new WebApplicationException(404));
```

The readTextFile() may return NULL in case of failed to open the needed file. 
However, code has no worry to null pointer in the middle of the data processing. 
Null pointer is handled at the very end of the pipeline. 

### Prefer injecting dependency over hard code dependency

I use Jersey framework's build-in dependency injection lib (HK2) to inject dependency of controllers.
The 'real' dependency implementation is decoupled from where it is be used.

``` java
public class PostsController {
    @Inject
    PostRepository postRepository;

    @Inject
    PaperAdapter paperAdapter;
    
    public void getPost() {
        // ...
    }
}
```

The PostController has no idea to the 'real' implementation of the PostRepository and PaperAdapter.
Those 2 implementations could be easily replaced for testing purpose.

### Prefer concise code over verbosity code

Programs must be written for people to read. I believe concise API is also readable and self-explanatory code.

``` java
 @Override
    public Optional<Paper> toCompletePage(Paper.Id id) {
        return from(id)
                .with(AppContext::realPagePath)
                .by(TextReader::completeRead);
    }
```
The code can be translated to plain English as "Convert to page content from paper id with page path by completed text reader".
I feel this is most concise code I could have for now. I cannot delete more word from them.

Does it sound good? Please leave comments on <a href="https://github.com/chengpo/my-blog/issues" target="_blank"> issues page </a>. <br>
Bye for now.

<!--eof-->
