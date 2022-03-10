package com.momo

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.quartz.*
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory
import java.time.LocalDateTime


fun scheduleCronJob(){
    val schedulerFactory = StdSchedulerFactory();
    val scheduler = schedulerFactory.scheduler;



    val job = JobBuilder.newJob(PayoutJob::class.java)
        .withIdentity("myJob", "group1")
        .build()

    val trigger = newTrigger()
        .withIdentity("trigger3", "group1")
        .withSchedule(cronSchedule(" 0 * * * * ? "))
        .forJob("myJob", "group1")
        .build()

    scheduler.scheduleJob(job, trigger);
    scheduler.start();
}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val stamp = LocalDateTime.now().toString()
    routing {
        get("/") {
            call.respondText(stamp)
        }
    }

    scheduleCronJob()


//    launch {
//        while(true) {
//            delay(6000)
//            val stamp = LocalDateTime.now().toString()
//
//
//        }
//    }
}

