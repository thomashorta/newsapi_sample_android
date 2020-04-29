package com.isansc.apollographqltestwithmockwebserverpoc.tools

object ServiceMocker {
    fun mockGetSources(){
        val resultJson = FileUtils.readJson("GetSourcesSuccess.json")
        ServerMock.mock(resultJson)
    }

    fun mockGetEverything(){
        val resultJson = FileUtils.readJson("GetEverythingSuccess.json")
        ServerMock.mock(resultJson)
    }
}