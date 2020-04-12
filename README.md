## about
Enigma was a revolutionary encryption tool used by the Germans during World War II. Numberphile provides a great [overview of Enigma's operation and mathematical complexity](https://www.youtube.com/watch?v=G2_Q9FoD-oQ). This project breaks the Enigma cipher in 3 steps:

1. **The Exploit** -- Enigma never enciphers a character to itself. Exploit this weakness by seeing where known common phrases, called "cribs" might exist. For example, many Nazi naval messages were known to begin with ANXKOMXADMXUUUBOOTE<sup>[1](#footnote1)</sup> ("An Kom Adm UUU Boote", after removing X's that are used as spaces<sup>[2](#footnote2)</sup>, which is abbreviated for "An Kommandie Admiral UUU Boote", translated "To Commanding Admiral of U-Boats"). So if the following encrypted message was intercepted:
    ```
    ENNRTCWAQPZAWXCPOREUMPOQXZDFRRTBMLOJTFRFF
    ```
    We know this message does not begin with ANXKOMXADMXUUUBOOTE because if Enigma _had_ in fact encrypted "ANXKOMXADMXUUUBOOTE...", the second letter would not be N as it is in this example. However, if this encrypted message was intercepted:
    ```
    RKHYGZSDCNBGKCWAVBQPLORTWCRWERKZTCJIKLAQNVFLPOOMPFCQKAQNNRQNVLWSDZZDHPXYUERIELWRVQICESCTKTJBGCBDYMPTGCRUMWYKVWHILUXPEIEVTVBCTMBRNVCCQZGWDNHMMHXQUAKIYPGYTJPRJTARPYHUWXEUIFQIUHY
    ```
    This message may very well begin with ANXKOMXADMXUUUBOOTE. The first character is not an A, the second letter is not an N, and so on. So ANXKOMXADMXUUUBOOTE is a crib we can use. 
ANXKOMXADMXUUUBOOTE is a simple crib that is only expected to be found at the beginning of the message, but others such as KEINEBESONDERENEREIGNISSEXZUXBERICHTEN ("No special occurrences to report") are commonly found anywhere throughout the message. In this case, we'd "drag" the crib along the message left to right and check each position to see if the crib is usable.

1. **Key Search** -- Launch a brute force attack that encrypts the message for every possible rotor and stecker cable combination, and see if the crib from step 1 appears. So for the above message, we know the decrypted message starts with ANXKOMXADMXUUUBOOTE. So if we set all our rotors to the A position, and plug the steckers in so that A is connected to B, C to D, E to F, and so on, we might get encrypt the first character to W. Since we know the correct rotor+stecker combination will yield an A in the first position, we move on to the next combination. retry, and so on:

    | **rotors** | **steckers** | **output** |  |
    |---|---|---|---|
    | AAA | A=>B C=>D E=>F G=>H... | W... | _first character is not the A from ANXKOMXADMXUUUBOOTE, abort and try another combination_ |
    | AAB | A=>B C=>D E=>F G=>H... | AR... | _second character is not the N from ANXKOMXADMXUUUBOOTE, abort and try another combination_ |
    | ... | ... | ... | _... couple million tries later ..._ |
    | XPV | B=>X D=>F G=>T M=>O... | T... | _nope, abort and try another combination_ |
    | XPW | B=>X D=>F G=>T M=>O... |  ANXKOMXADQ... | _close, abort and try another combination_ |
    | ... | ... | ... | _... couple million tries later ..._ |
    | CRO | C=>K L=>O R=>Z W=>Y... |  ANXKOMXADMXUUUBOOTE... | _Success! So we know that these are the correct settings!_<sup>[3](#footnote3)</sup> |

    This step simulates the [Turing Bombe](https://en.wikipedia.org/wiki/Bombe) . This implementation runs up to 8 concurrent threads in the brute force attack. The division of labor between the threads is handled by assigning each thread a range of the leftmost rotor ("slow rotor"). For example, if there are two threads, one handles A through M on the leftmost rotor, and the other handles N through Z.

1. **Decrypt the rest of the message** -- Now that we know the encryption "key" (settings), reset the machine to the settings from step 2 and decrypt the entire message this time, rather than just the portion covered by the crib. We get:<br>
    ```
   ANXKOMXADMXUUUBOOTEYFDUUUAUSBXYFDUUUOSTYSSSMMMHHHSSSOSTAZWWFUNFXUUUFLOTTXYFUNFXUUUFLOTTXYHAKAXKIELVONVONTORPXFANGBOOTEINSNEUNXXEINSZWOUHRJKIELWEINGELAUFENYFFFTTTBLEIBTBESETZTR
    ```
Translation: To Commanding Admiral of U Boats, Comsubs Training, Comsubs East, SMHS East, 25th Submarine Flotilla, 5th Submarine Flotilla, [and] Port Captain Kiel from Torpedo Recovery Boat 19: At 12[00] hours entered port at Kiel, radio remains manned.
<br>
<br>
Historically, Enigma's settings were changed daily. Once the key was discovered (step 2), the rest of the day's messages could be decrypted by skipping to step 3. This demo uses one sample message at a time.

## usage
1. requires java 1.8 or later
1. `git clone git@github.com:mikerodonnell/enigma_multithread_crypto_breaker.git`
1. `cd enigma_multithread_crypto_breaker`
1. `mvn clean install`
1. `mvn exec:java`

## how is this different from other Enigma implementations?
* Most Enigma implementations simulate the encryption/decryption for a given key, but don't include functionality to break the encryption.
* Many encryption-breaking Enigma implementations use a standard dictionary attack, instead of the more historically accurate crib drag attack.
* Many encryption-breaking Enigma implementations only use the rotors, and not the steckerboard. An historically accurate 10-pair steckerboard increases the entropy of the encryption by a factor of 150 _trillion_!
* Most implementations that do support at least a partial steckerboard are single-threaded. Using parallel threads, each responsible for a subset of the total combinations, improves performance on multi-core machines.

## math!
One of the major challenges of implementing the Enigma attack is to efficiently iterate over each possible stecker combination. In practice, the M3 Enigma used 10 steckered pairs, though 0 through 13 are possible. How many combinations should we expect? Consider the simplest case of a single stecker cable:
Combinations will include A=>B, then A=>B, A=>C, etc. all the way to A=>Z. A=>B is the same as B=>A since the stecker cables are just bidirectional electric connections. So when we're done checking all the A combinations, we can start the B combinations with B=>C:
```
A=>B A=>C A=>D A=>E A=>F ... A=>Z // 25 combinations
     B=>C B=>D B=>E B=>F ... B=>Z // 24 combinations
          C=>D C=>E C=>F ... C=>Z // 23 combinations
                                  // . . .
                             Y=>Z // 1 combination
```

So we have a pattern of 25 + 24 + 23 + ... + 3 + 2 + 1 for the 26 letter alphabet, which we can describe as a [Guass Sum](https://www.wyzant.com/resources/blogs/277085/mathematical_journeys_carl_gauss_and_the_sum_of_an_arithmetic_series) for n=25:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(25 * 25+1)/2 = 325<br>
If we add one additional cable, it will start at `C=>D`. This is 2 positions to the right of our first cable's start position, so the second cable's Guass Sum is for n=23, or 276. So for two cables we have a total of 325*276 = 89,700 combinations for only two cables. Some of these are invalid since, for example, `A=>R` and `B=>R` can't co-exist. The algorithm skips over invalid combinations and only spends processing time on valid combinations.

## performance
The time to break the cipher varies with the number of stecker cables used. On a reasonable 4 or 8 core machine:<br>
&nbsp;&nbsp;&nbsp;&nbsp;0-1 stecker pairs **&rarr;** seconds<br>
&nbsp;&nbsp;&nbsp;&nbsp;2-3 stecker pairs **&rarr;** minutes<br>
&nbsp;&nbsp;&nbsp;&nbsp;4-6 stecker pairs **&rarr;** hours<br>
&nbsp;&nbsp;&nbsp;&nbsp;7-10 stecker pairs **&rarr;** days!<br>

The attack is processor intensive and uses very little memory. Benchmarks with [jstat](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html) show total Java heap consumption hovering around 60 kB regardless of thread count.

## tools
* [Maven Surefire plugin](https://maven.apache.org/surefire/maven-surefire-plugin) _recent versions of Surefire allow terminating testing upon first failure, which is useful for projects with long-running tests._
* [Maven Exec plugin](http://www.mojohaus.org/exec-maven-plugin) _for easy usage through command line._
* [JUnit 5](http://junit.org)
* [Apache Commons Lang](https://commons.apache.org/proper/commons-lang)
* [jstat](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html) _for memory benchmarking_

## footnotes
<a name="footnote1">1</a>. Although this implementation is based on the Army M3 Enigma machine, some cribs and sample messages more applicable to the Naval M4 Enigma or other Enigma variations are used as well.<br>
<a name="footnote2">2</a>. Numbers were spelled out in Enigma messages. Additionally, a number of reserved character sequences are used in Enigma messages, including:

&nbsp;&nbsp;&nbsp;&nbsp;X **&rarr; .** or (space)<br>
&nbsp;&nbsp;&nbsp;&nbsp;UUU **&rarr;** U-Boat<br>
&nbsp;&nbsp;&nbsp;&nbsp;Y **&rarr; ,**<br>
&nbsp;&nbsp;&nbsp;&nbsp;[more](http://www.codesandciphers.org.uk/documents/egenproc/egenproc.pdf)

<a name="footnote3">3</a>. Several different combinations might yield the same crib, especially for shorter cribs. The cipher text would then be decrypted which each of the combinations in inspected manually; one would contain German military instructions and the others would contain gibberish. Longer cribs are preferable as the increased entropy results in fewer false positives.

## credits and further reading
* [https://en.wikipedia.org/wiki/Enigma_machine](https://en.wikipedia.org/wiki/Enigma_machine)
* [https://en.wikipedia.org/wiki/Enigma_rotor_details](https://en.wikipedia.org/wiki/Enigma_rotor_details)
* [www.enigma.hoerenberg.com](www.enigma.hoerenberg.com)
* [www.youtube.com/user/numberphile](https://www.youtube.com/user/numberphile)
* [www.wyzant.com/resources/blogs/277085/mathematical_journeys_carl_gauss_and_the_sum_of_an_arithmetic_series](https://www.wyzant.com/resources/blogs/277085/mathematical_journeys_carl_gauss_and_the_sum_of_an_arithmetic_series)
* [www.imdb.com/title/tt2084970](http://www.imdb.com/title/tt2084970)
