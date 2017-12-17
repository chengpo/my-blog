I learnt a useful git command today.


Let's say I am working on a large code refactoring which would introduce a long change list.<br>
For the sake of maintenance, I make a working branch from my 'master' branch:
```
$ git checkout -b code-refactoring
```

Then I keep making small commit on my working branch. Obviously, I will end up with a bunch of
small commits on the working branch.<br>
After finish all the changes, I want to merge my commits back to the 'master' branch.


But wait a second. To keep the commit log clean, I'd all the relevant changes be combined into one commit,
then merge it into my 'master' branch.


To do so, all I need is just spell the magic commands:
```
$ git checkout master
$ git merge --squash code-refactoring
$ git commit
```

Voila, all the commits from code-refactoring branch be squashed into one large commit.<br>
That's it.

<!--eof-->