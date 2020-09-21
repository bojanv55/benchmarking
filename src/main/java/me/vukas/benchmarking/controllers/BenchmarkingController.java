package me.vukas.benchmarking.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import lombok.AllArgsConstructor;
import me.vukas.benchmarking.cpu.SerializationBenchmarks;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BenchmarkingController {

  private static boolean done = false;

  @PostMapping("/cpu")
  public void startSerializationBenchmarks() throws RunnerException {
    StringBuilder classpath = new StringBuilder(System.getProperty("java.class.path"));
 //     classpath.append(":/var/lib/component/java/lib/service.jar;jmh-core-1.25.2.jar/*");
 //   System.setProperty("java.class.path", classpath.toString());
 //   System.out.println("CLASSPATH IS " + classpath.toString());

    try {
      if(!done) {
        InputStream resourceAsStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("jmh-core-1.25.2" + ".jar");
        final File tempFile = File.createTempFile("temp", ".jar");
        //tempFile.deleteOnExit();  // you can delete the temp file or not
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
          IOUtils.copy(resourceAsStream, out);
        }
        IOUtils.closeQuietly(resourceAsStream);
        URL url = tempFile.toURI().toURL();
//      URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
//      urlClassLoader.loadClass()
        classpath = new StringBuilder(System.getProperty("java.class.path"));
        classpath.append(":" + "/var/lib/component/java/lib/service.jar");
        classpath.append(":" + url.getPath());
        System.setProperty("java.class.path", classpath.toString());
        done = true;
      }
    }
    catch (Exception e){
      System.out.println(e.getMessage());
    }

    Options opts = new OptionsBuilder()
        .include(SerializationBenchmarks.class.getSimpleName())
        .param("seed", "123")
        .param("numberOfMatches", "123")
        .warmupIterations(5)
        .measurementIterations(5)
        .measurementTime(TimeValue.milliseconds(5000))
        .forks(0)
        .build();

    new Runner(opts).run();
  }

}
