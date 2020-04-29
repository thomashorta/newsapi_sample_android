package com.isansc.apollographqltestwithmockwebserverpoc.tools

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

object ServerMock {
    private val server = MockWebServer()
    private var isServerStarted = false

    fun startServer(){
        if(!isServerStarted) {
            /**
             * Setting up mockWebServer at localhost:9900.
             */

            // start the server at port 9900
            server.start(9900)
            isServerStarted = true
        }
    }

    fun shutDownServer(){
        server.shutdown()
    }

    fun getUrl(path:String): String {
        return server.url(path).toString()
    }

    fun mock(body: String){
        // Enqueue the response you want the server to respond with when queried in the course of the test.
        server.enqueue(MockResponse().setBody(body))
    }

}