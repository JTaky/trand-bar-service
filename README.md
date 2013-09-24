trand-bar-service
==================

1. Builds Trend Bars based upon received quotes. 
2. Provides trend bars history by a request

Solution is not for production. There is exist a lot of performance botlneck. Implementation is not scalable. 
I focused on design and testability. I didn't tested implementation carefully in integration. 
I know a lot of cases didn't covered with Unit tests.
I have marked with 'TODO' probable problems.
For unit testing used jUnit. Of cource nUnit is better for multithreading testing.
For DI I used Guice.

target jvm - 1.7
build system - maven