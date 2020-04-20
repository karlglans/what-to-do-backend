package com.karlglans.whattodo.controllers;

class TestDataSetup1 {
  // valid auth header for token secret: 'aaa', sub: '1'
  static String validAuthHeaderUser1 = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ" +
    "3aGF0dG9kbyIsInN1YiI6MSwiaWF0IjoxNTg1NDg4MzcxfQ.yFBJrc9h-Hs9q5ns7DKwneLUNBDpMdSIQbTwX-6LepM";
  // valid auth header for token secret: 'aaa', sub: '2'
  static String validAuthHeaderUser2 = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ"+
    "3aGF0dG9kbyIsInN1YiI6MiwiaWF0IjoxNTg3Mjg2MTQ0fQ.KSXmnLIonf7okRNh7f17qbI3wQYPeDuzI1wRFpOgJ5M";

  static String validAuthHeaderButMissingUser = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ" +
    "pc3MiOiJ3aGF0dG9kbyIsInN1YiI6OTk5LCJpYXQiOjE1ODczNzIxMjl9.6MnXs43Ne-Z12J3mkMKpUcD0pc6kbrokKmRW6oULno8";

  static int someTodoIdForTodoByUser2 = 106;
}
