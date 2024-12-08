package com.github.smmousavi.common.test

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultInitialize

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TestInitialize