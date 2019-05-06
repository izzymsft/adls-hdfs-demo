package com.microsoft.ocp.analysis

import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.{RuntimeConfig, SparkSession}
import org.apache.hadoop.fs.{FileSystem, Path}

object DirectoryListingExample {

  def main(args: Array[String]): Unit = {

    val hdfsPath = "hdfs://hn0-spkdem.uztxw0d0btwu1a10j04zhy13xd.bx.internal.cloudapp.net:8020/tmp"

    val adlsGen2Path = "abfss://spkdemo08-2019-05-06t15-04-50-520z@hdiadlsgen2.dfs.core.windows.net/"

    val spark = SparkSession
      .builder()
      .appName("Spark-HDFS-Directory-Listing -Example")
      .config("fs.azure.account.auth.type.hdiadlsgen2.dfs.core.windows.net", "OAuth")
      .config("hive.metastore.warehouse.dir", "/hive/warehouse")
      .getOrCreate()

    val hadoopConfiguration: Configuration = spark.sparkContext.hadoopConfiguration
    val runtimeConfig: RuntimeConfig = spark.conf

    // File System Handlers for hdfs protocol
    runtimeConfig.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem")
    runtimeConfig.set("fs.AbstractFileSystem.hdfs.impl", "org.apache.hadoop.fs.Hdfs")

    // Authentication Type for SP
    runtimeConfig.set("fs.azure.account.auth.type.hdiadlsgen2.dfs.core.windows.net", "OAuth")

    // Authentication Provider
    runtimeConfig.set("fs.azure.account.oauth.provider.type.hdiadlsgen2.dfs.core.windows.net", "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider")

    // Client ID for Service Principal
    runtimeConfig.set("fs.azure.account.oauth2.client.id.hdiadlsgen2.dfs.core.windows.net", "8f4f6264-9693-85de-9d4c-c6474e4f35fb")

    // Secret for Service Principal
    runtimeConfig.set("fs.azure.account.oauth2.client.secret.hdiadlsgen2.dfs.core.windows.net", "c325e21a-0154-5eac-a9e7-53157dad808b")

    // Token Endpoint with the guid of your tenant id
    runtimeConfig.set("fs.azure.account.oauth2.client.endpoint.hdiadlsgen2.dfs.core.windows.net", "https://login.microsoftonline.com/82f988bf-86f1-41af-51ab-2d7cd011db47/oauth2/token")

    // Creates the Remote File System during Initialization
    runtimeConfig.set("fs.azure.createRemoteFileSystemDuringInitialization", "false")



    println("==========================================================================================================")
    println("Printing Out contents of " + adlsGen2Path)
    // Print out the list of directories or files in this path
    FileSystem.get(hadoopConfiguration)
      .listStatus(new Path(adlsGen2Path))
      .foreach(x => println("ADLS_Gen2 Path: " + x.getPath))

    println("==========================================================================================================")

    val listStatus = FileSystem.get(new java.net.URI(hdfsPath), hadoopConfiguration)
      .listFiles(new org.apache.hadoop.fs.Path(hdfsPath), true)

    println("==========================================================================================================")
    println("Printing Out contents of " + hdfsPath)
    // Loop through all the descendants of the HDFS Path
    while (listStatus.hasNext()) {
      println("HDFS Path: " + listStatus.next().getPath().toString)
    }

    println("==========================================================================================================")

    // Shut down the job
    spark.stop();

  }

}
