package sample.ether.school

import android.widget.TextView
import arch.ether.Ether
import arch.ether.observableOf
import io.reactivex.android.schedulers.AndroidSchedulers

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
