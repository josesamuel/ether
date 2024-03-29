# Ether

Ether is a simple [PubSub](https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern) architecture framework for publishing and subscribing data. 

Problems that are solved using model-view architectures can be solved much easily using Ether framework using the PubSub architecture.

**Why Ether?**
--------

Let us look at a common model-view architecture - MVP

![MVP Pattern](docs/mvp.png)

MVP succeeds in separating Model and View, but Presenter is left with the responsibility of gluing this together. Presenter will be dependent on both Model and View. 

As the complexity of your code grows, so is the complexity of Presenter. 

But what exactly is Presenter's role? At its core, it get the data from Model and pass it to View

Imagine if you could just 

* Focus on how to produce the data -- without worrying about who consumes it or how it is send to consumer
* Focus on how to consume the data -- without worrying about who produces it or how it is delivered

This allows you to just focus on what is important for you -- Producing the data, and consume it without worrying about how the data moves. 
 
This is what Ether provides

With Ether the above architecture can be simplified as

![MVP Pattern](docs/ether.png)



Here is a comparison of how a simple problem is solved using [MVP](https://github.com/josesamuel/ether/tree/master/MVPSample/src/main/java/sample/mvp) vs [Ether](https://github.com/josesamuel/ether/tree/master/EtherSample/src/main/java/sample/ether) PubSub



**Using Ether**
--------


* **Define the data** class, annotate it with <mark>@EtherData</mark>, specify the triggers for the data. 

```kotlin

//Data that will be shared

@EtherData(triggerType = CityDataTrigger::class)
data class SchoolData(val schoolCount: Int, val studentCount: Int)


//Define the triggers that should produce the above data
/**Triggers to produce [SchoolData]*/
sealed class CityDataTrigger
object Austin : CityDataTrigger()
object Dallas : CityDataTrigger()

```

* **Produce the data**

```groovy
/**
 * Publisher of [SchoolData] by extending the auto generated [AbstractSchoolDataPublisher]
 *
 * A Publisher can also be created using the auto generated [SchoolDataPublisher]
 */
class SchoolDataPublisher : AbstractSchoolDataPublisher() {

    /**
     * Gets called when the trigger for [SchoolData] is sent to the [Ether]
     */
    override fun onPublisherTrigger(trigger: CityDataTrigger) {
        when (trigger) {
            Austin -> publish(SchoolData(20, 800))
            Dallas -> publish(SchoolData(45, 1900))
        }
    }
}
```

* **Consume the data**

```groovy
/**
 * Update the given school views based on the [SchoolData] received from [Ether]
 *
 * Other ways to subscribe is [Ether.subscriberOf]
 */
fun initSchoolSubscriber(schoolCount: TextView, studentCount: TextView) =

        Ether.observableOf(SchoolData::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    schoolCount.text = it.schoolCount.toString()
                    studentCount.text = it.studentCount.toString()
                }

```




Getting Ether
--------


Gradle dependency

```groovy
dependencies {

	//Java library
	implementation 'com.josesamuel:ether:1.0.4'
    kapt 'com.josesamuel:ether-processor:1.0.4'
   
    //If you want observable extensions (RxJava) on top of the above, use the following instead 
    implementation 'com.josesamuel:ether-observable:1.0.4'
    kapt 'com.josesamuel:ether-observable-processor:1.0.4'

}
```


License
-------

    Copyright 2018 Joseph Samuel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


