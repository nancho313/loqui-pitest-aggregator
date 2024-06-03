package com.nancho313.loqui.pitestaggregator;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.pitest.aggregate.ReportAggregationException;
import org.pitest.aggregate.ReportAggregator;
import org.pitest.mutationtest.config.DatedDirectoryReportDirCreationStrategy;
import org.pitest.mutationtest.config.DirectoryResultOutputStrategy;

import java.util.List;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.NONE)
public class PitestAggregator extends AbstractMojo {

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(property = "outputDirectory", defaultValue = "${basedir}/target")
  private String outputDirectory;

  @Override
  public void execute() throws MojoFailureException {

    var modules = project.getDependencies().stream().map(Dependency::getArtifactId).toList();
    var workingDirectory = project.getParent().getBasedir().getAbsolutePath();
    ReportAggregator.Builder raBuilder = ReportAggregator.builder();

    var moduleConfigurations = modules.stream().map(module -> new Module(workingDirectory, module)).toList();

    for (Module module : moduleConfigurations) {
      raBuilder.addMutationResultsFile(module.getMutationResultsFile())
              .addLineCoverageFile(module.getLineCoverageFile())
              .addSourceCodeDirectory(module.getSourceDirectory())
              .addCompiledCodeDirectory(module.getBinaryDirectory());
    }
    var strategy = new DatedDirectoryReportDirCreationStrategy();
    raBuilder.resultOutputStrategy(new DirectoryResultOutputStrategy(outputDirectory, strategy));
    try {
      raBuilder.build().aggregateReport();
    } catch (ReportAggregationException e) {
      throw new MojoFailureException(e);
    }
  }
}
