package com.hendraanggrian.packaging

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import java.io.File

interface PackSpec {

    //region Generic Options
    /**
     * Version of the application and/or package`.
     * Default is project's version.
     */
    val appVersion: Property<String>

    /** Copyright for the application. */
    val copyright: Property<String>

    /** Description of the application. */
    val appDescription: Property<String>

    /**
     * Name of the application and/or package.
     * Default is project's name.
     */
    val appName: Property<String>

    /**
     * Path where generated output file is placed.
     * Default is `build/install` in project directory.
     */
    val outputDirectory: DirectoryProperty

    /** Vendor of the application. */
    val vendor: Property<String>

    /**
     * Enables verbose output.
     * Default is false.
     */
    val verbose: Property<Boolean>
    //endregion

    //region Options for creating the runtime image
    /** This module list, along with the main module (if specified) will be passed to jlink as the --add-module argument. */
    val addModules: ListProperty<String>

    /** Each path is either a directory of modules or the path to a modular jar. */
    val modulePath: ListProperty<File>

    /** Pass on --bind-services option to jlink (which will link in service provider modules and their dependences). */
    val bindServices: Property<String>

    /** Path of the predefined runtime image that will be copied into the application image. */
    val runtimeImage: RegularFileProperty
    //endregion

    //region Options for creating the application image
    /** Path of the icon of the application bundle. */
    val icon: RegularFileProperty

    /** Path of the input directory that contains the files to be packaged. */
    val inputDirectory: DirectoryProperty
    //endregion

    //region Options for creating the application launcher(s)
    /** Name of launcher, and a path to a Properties file that contains a list of key, value pairs. */
    val addLauncher: RegularFileProperty

    /** Command line arguments to pass to the main class if no command line arguments are given to the launcher. */
    val arguments: ListProperty<String>

    /** Options to pass to the Java runtime. */
    val javaOptions: ListProperty<String>

    /** Qualified name of the application main class to execute. */
    val mainClass: Property<String>

    /**
     * The main JAR of the application; containing the main class (specified as a path relative to the input path).
     * Default is `$projectName-$projectVersion.jar`
     */
    val mainJar: Property<String>

    /** The main module (and optionally main class) of the application This module must be located on the module path. */
    val module: Property<String>
    //endregion

    //region Options for creating the application package
    /** Location of the predefined application image that is used to build an installable package. */
    val appImage: RegularFileProperty

    /** Path to a Properties file that contains list of key, value pairs. */
    val fileAssociations: RegularFileProperty

    /** Absolute path of the installation directory of the application on OS X or Linux. */
    val installDirectory: DirectoryProperty

    /** Path to the license file. */
    val licenseFile: RegularFileProperty

    /** Path to override jpackage resources. */
    val resourcesDirectory: DirectoryProperty
    //endregion
}
