#File search util

##Calling util

```
sbt> runMain co.beetroot.Main <folder-to-analyze>
5 files read in directory C:\Temp\Books

search> anxious lobster
Matches found: 766-0.txt: 100.0% 158-0.txt: 43.06% 1342-0.txt: 36.12% 1400-0.txt: 29.17% 121-0.txt: 26.39%

search> computer
No matches found

search>:quit
```

You can search for several words separated by a space.

If the match is found file names with a score are output (sorted descending).

To exit please use `:quit` command

##Score counting

Score is calculated according to the following heuristics:

- each file gets score for each word. This intermediate score is in (0.0; 1.0] range.
- scores for all words are summed for each file; so file can get score N maximum, where N is a number of words
- previous scores are normalized and cast to percents; so the file with the highest word count gets 100%

##TODO
Add more error handling

##Testing

Just run `test` or run subset of specs if needed.