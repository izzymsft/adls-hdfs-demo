## Setting up a Spark 2.3.x Cluster on HD1 3.6 with Data Lake Gen 2

## Step 1 Login to the Azure account
``shell``
az login
`

## Step 2 Specify the subscription
``shell``
az account set --subscription "86d61f02-2fcf-4a37-bb6e-cc2c2db9f67f"
`

## Step 3 Create resource group for the HDI cluster and Managed Identity. Took about 2 seconds.
``shell``
az group create --name spark-demo-deployment-rg --location eastus
`

## Step 4a Create managed identity. Took about 2 minutes.
``shell``
az identity create -g spark-demo-deployment-rg -n spark-hdinsight-msi
`

## Step 4b Create Service Principal with Password
``shell``
az ad sp create-for-rbac --role="Contributor" --name="spark-author"
`

## Step 5 Assign Contributor Role to the Managed Identity using the ClientId of the Managed Identity
``shell``
az role assignment create --assignee eca5d191-3185-408b-920f-9a5459e26875 --role Contributor
`

## Step 6 This extension provides a preview for upcoming storage features
``shell``
az extension add --name storage-preview
`

## Step 7a - Create the Storage Account that will be used by the cluster 
``shell``
az storage account create --name hdiadlsgen2 --resource-group spark-demo-deployment-rg --location eastus --sku Standard_LRS --kind StorageV2 --hierarchical-namespace true
`

## Step 7b - sign in to the portal. Add the new user-assigned managed identity to the Storage Blob Data Owner role on the storage account, as described in step 3 under Using the Azure portal.
``shell``
https://docs.microsoft.com/en-us/azure/hdinsight/hdinsight-hadoop-use-data-lake-storage-gen2
`

## Step 8 - Create the HDInsight 3.6 Spark 2.3.0 Cluster. Took approximately 10 minutes 16 seconds for deployment to complete.
``shell``
az group deployment create --name HDInsightADLSGen2Deployment20190506.1 --resource-group spark-demo-deployment-rg --template-file hdi-adls-gen2-template.json --parameters hdi-adls-gen2-parameters.json 
`

## Step 9 - Get Into the Server via SSH and test out access to the ADLS Gen 2 Storage Account 
``shell``
ssh sshuser@spkdemo08-ssh.azurehdinsight.net
`

## Step 10 a - List directories in the Storage Account's default file system used for the cluster
``shell``
hdfs dfs -ls /
`

## Step 10 b - List directories in the Storage Account's default file system used for the cluster using Fully Qualified Path Name
``shell``
hdfs dfs -ls abfss://spkdemo08-2019-05-06t15-04-50-518z@hdiadlsgen2.dfs.core.windows.net/
hdfs dfs -ls hdfs://hn0-spkdem.uztxw0d0btwu1a10j04zhy13xd.bx.internal.cloudapp.net/tmp/hive
hdfs dfs -ls abfss://sample8b@hdiadlsgen2c.dfs.core.windows.net/
`

## Step 11 - Compile the Code, Upload it to the Cluster and Submit the Spark Job
``shell``
spark-submit --class com.microsoft.ocp.analysis.DirectoryListingExample --master local[4] 
`
