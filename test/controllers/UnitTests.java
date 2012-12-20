package controllers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import userselection.PaginationFilterTest;
import userselection.PartialSortingTest;


@RunWith(Suite.class)
@SuiteClasses({ PartialSortingTest.class, PaginationFilterTest.class })
public class UnitTests {

}
