package controllers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import forms.PaginationFilterTest;
import forms.PartialSortingTest;


@RunWith(Suite.class)
@SuiteClasses({ PartialSortingTest.class, PaginationFilterTest.class })
public class UnitTests {

}
