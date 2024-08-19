package com.hanggrian.packaging

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import java.io.File

/**
 * A specification for packaging native bundler. Sub-interface of this interface should target a
 * specific platform.
 */
@PackConfigurationDsl
public interface PackSpec {
    //region Generic Options

    /** Version of the application and/or package`. Default is project's version. */
    public val appVersion: Property<String>

    /** Copyright for the application. */
    public val copyright: Property<String>

    /** Description of the application. */
    public val appDescription: Property<String>

    /** Name of the application and/or package. Default is project's name. */
    public val appName: Property<String>

    /**
     * Path where generated output file is placed. Default is `build/install` in project directory.
     */
    public val outputDirectory: DirectoryProperty

    /** Vendor of the application. */
    public val vendor: Property<String>

    /** Enables verbose output. Default is false. */
    public val verbose: Property<Boolean>
    //endregion

    //region Options for creating the runtime image

    /**
     * This module list, along with the main module (if specified) will be passed to jlink as
     * the `--add-module` argument.
     */
    public val modules: SetProperty<String>

    /** Each path is either a directory of modules or the path to a modular jar. */
    public val modulePaths: SetProperty<File>

    /**
     * Pass on --bind-services option to jlink (which will link in service provider modules and
     * their dependencies).
     */
    public val bindServices: Property<String>

    /** Path of the predefined runtime image that will be copied into the application image. */
    public val runtimeImage: RegularFileProperty
    //endregion

    //region Options for creating the application image

    /** Path of the icon of the application bundle. */
    public val icon: RegularFileProperty

    /** Path of the input directory that contains the files to be packaged. */
    public val inputDirectory: DirectoryProperty
    //endregion

    //region Options for creating the application launcher(s)

    /**
     * Name of launcher, and a path to a Properties file that contains a list of key, value pairs.
     */
    public val launcher: RegularFileProperty

    /**
     * Command line arguments to pass to the main class if no command line arguments are given to
     * the launcher.
     */
    public val args: ListProperty<String>

    /** Options to pass to the Java runtime. */
    public val javaArgs: ListProperty<String>

    /** Qualified name of the application main class to execute. */
    public val mainClass: Property<String>

    /**
     * The main JAR of the application; containing the main class (specified as a path relative to
     * the input path). Default is `$projectName-$projectVersion.jar`
     */
    public val mainJar: Property<String>

    /**
     * The main module (and optionally main class) of the application This module must be located on
     * the module path.
     */
    public val mainModule: Property<String>
    //endregion

    //region Options for creating the application package

    /**
     * Location of the predefined application image that is used to build an installable package.
     */
    public val appImage: RegularFileProperty

    /** Path to a Properties file that contains list of key, value pairs. */
    public val fileAssociations: RegularFileProperty

    /** Absolute path of the installation directory of the application on OS X or Linux. */
    public val installDirectory: DirectoryProperty

    /** Path to the license file. */
    public val license: RegularFileProperty

    /** Path to override jpackage resources, *not application resources*. */
    public val resourcesDirectory: DirectoryProperty
    //endregion
}
