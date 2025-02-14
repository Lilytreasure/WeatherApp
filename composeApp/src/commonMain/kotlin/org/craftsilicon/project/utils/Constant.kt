package org.craftsilicon.project.utils

import org.craftsilicon.project.ApiKeys


object Constant {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/forecast"
    const val TIME_OUT: Long = 150000
    /**
     *Not good practise to keep the api key here
     * How ever you wont be able to run the app without the key that was given to me
     * Add  the Api key in your local.properties file in the format(API_KEY=actual plain key here without quotes)
     */
    const val API_KEY= ApiKeys.API_KEY

}