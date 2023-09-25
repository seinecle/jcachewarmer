# A cache warmer

Read [the blog post on this cache warmer](https://nocodefunctions.com/blog/cache-warmer/) for fuller details.


### Instructions for distribution and use
The program is compiled as a single jar file with no dependencies, just 19kb of size.
It is located in the `target` folder.

### Properties files:

These two property files, visible here on Github should be located in the same folder as the `CacheWarmer-1.0.jar` file

* `cache-warmer.properties`
    - the user agent to simulate a visit by a mobile phone
    - the user agent to simulate a visit by a desktop browser
    - number-of-pages-visited-concurrently-in-one-batch=10
    - page-loading-timeout-in-milliseconds=5000
    - interval-between-two-batches-in-milliseconds=1000
    - interval-between-two-cache-warmup-in-minutes=30

* `site-map-urls.txt`
    - This file contains one or several urls of the sitemaps that will be visited by the cache warmer.

The warmer will visit the urls listed in two xml tags:

- `<loc>https://www.example.fr</loc>`
- `<image:loc>https://www.example.fr/img/4.jpg</image:loc>`

### Log files

While running, the cache warmer logs data in two files:

- error-log.txt
- info-log.txt

#### If you want to create a Java runtime, so that users don't have to install it
`jlink --module-path target --add-modules jcachewarmer --output out`

#### For the user

Run: `nohup java --module-path . --module jcachewarmer/net.clementlevallois.cachewarmer.controller.Controller &`

That's it.
The program will read the properties in the two property files and do its thing.
It will not stop, runs for ever.


## License
CC-BY-4.0