# pekko sample 2

- Starting and stopping actors.
- When we stopped actor 'parent', it stopped its child's child actor 'second', then actor 'first' before stopping itself. 
- This ordering is strict, all PostStop signals of the children are processed before the PostStop signal of the parent is processed..