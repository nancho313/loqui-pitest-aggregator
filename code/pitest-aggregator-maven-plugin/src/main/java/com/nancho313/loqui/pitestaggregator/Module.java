package com.nancho313.loqui.pitestaggregator;

import java.io.File;

public class Module {

  private final String workingDirectory;

  private final String name;

  public Module(String workingDirectory, String name) {
    this.workingDirectory = workingDirectory;
    this.name = name;
  }

  public File getMutationResultsFile() {

    return new File(getModulePitReports() + "/mutations.xml");
  }

  public File getLineCoverageFile() {

    return new File(getModulePitReports() + "/linecoverage.xml");
  }

  public File getSourceDirectory() {

    return new File(getModulePath() + "/src/main/java");
  }

  public File getBinaryDirectory() {

    return new File(getModuleTargetPath() + "/classes");
  }

  private String getModulePitReports() {

    return getModuleTargetPath() + "/pit-reports";
  }

  private String getModuleTargetPath() {

    return getModulePath() + "/target";
  }

  private String getModulePath() {

    return workingDirectory + "/" + name;
  }
}
