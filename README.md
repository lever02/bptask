# BigPanda Task
<img style="height: 500px;width: 366px;" src='https://media1.giphy.com/media/uIHtx5ski357O/giphy.gif'/>

for word stats use : curl http://localhost:8080/pandaEvent/wordStats/ <br/>
for a specific word stats use : curl http://localhost:8080/pandaEvent/wordStats/your_word_here <br/>
for event type stats use : curl http://localhost:8080/pandaEvent/eventTypeStats/ <br/>
for a specific type stats use : curl http://localhost:8080/pandaEvent/eventTypeStats/your_type_here <br/>

In order to run the application you can just preform the "run" task
simply by running "<b>./gradlew run</b>"

by default the app will recognize which os your running and choose the proper generator
if you want to run different generator other than the default one
please provide the following environment variable "<b>panda_generator_path</b>"
for example (panda_generator_path = "/Users/erez/Downloads/generator-macosx-amd64")  

3 things i would improve : 
1. i would not use in memory map to store the stat and i would probably go with elastic search
and index those event to the same index
will get the easily with a query or even kibana to show the stats
2. i would improve exception handling and logs
3. add more unit and integration tests
also i chose reactor since it comes with spring boot 2 and i'm already familiar with it
but i was missing onErrorResumeWithNext like in RxJava
that why i didn't use jackson2JsonDecoder and mapped the string instead
