lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .settings(
      name := """boardyoshino""",
      organization := "com.example",
      
      version := "1.0-SNAPSHOT",
      
      scalaVersion := "2.13.8",
      
      libraryDependencies ++= Seq(
          guice,
          javaJdbc,
          "com.h2database" % "h2" % "1.4.192",
          evolutions,
          "org.hamcrest" % "hamcrest-all" % "1.3",
          ),
    )