package com.aws.peach.interfaces.api

import spock.lang.Specification

class HelloControllerTest extends Specification {
    def "simple test"() {
        expect:
        def result = new HelloController().hello()
        result == "hello"
    }
}
