# manson

**Not finished, in progress.**

A Json Library in Kotlin to perform high-level transformations.


**Merge jsons together**

```kotlin
val jsonMerge = Json(
"""
{
    "b" : 12,
    "a" : {
        "b" : "c", 
        "aa" :  {
            "b" : [1,2,12] 
            } 
    }
}
""",
"""
{
    "b" : 13,
    "a" : { 
        "d" : "aaa", 
            "aa" :  {
            "b" : [1,2,5,6,7,9]
        }
    }
}
""",
"{ \"test\": 1324 }"
).transform(
    TransformerFactory.merge(
        MergeOption(
            arrayMergeStrategy = ArrayMergeStrategy.MERGE_AND_REMOVE_DUPLICATES
        )
    )
)
```
jsonObject.getJsons() contains :
```json
{
    "b": 13,
    "a": {
        "b":"c",
        "aa": {
          "b":[1,2,5,6,7,9,12]
        },
        "d":"aaa"
    },
    "test":1324
}
```

**JsonPath object transformation**

```kotlin
val json = Json( """
        {
        "b" : 12,
        "a" : { 
            "b" : "c", 
            "aa" : [ {"b" : [1,2,3] } ]
        }, 
        "d" : ["dd"]
    }
""")
val jsonObject = json.transform(
    TransformerFactory.jsonObject(
        "numeric" to "$.a.aa[0].b[2]",
        "object" to "$.a",
        "array" to "$.a.aa[0].b",
        "char" to "$.a.b",
        "arrayChar" to "$.d"
    )
);

```
jsonObject.getJsons() contains :
```json
{
    "numeric":3,
    "b":"c",
    "aa": [ { "b": [1,2,3] }],
    "array": [1,2,3],
    "char":"c",
    "arrayChar": ["dd"]
}
```

**Perform transformations in parallel then join :**
```kotlin
val jsonMerge = Json(
    """
    {
        "b" : 12,
        "a" : {
            "b" : "c", 
            "aa" :  {
                "b" : [1,2,3] 
                } 
            },
        "d" : 10
   }
   """
)
// Execute each transformers in parallel then join the result with another transformer
.parallelTransform(
    TransformerFactory.fieldValuesExtraction("aa"),
    TransformerFactory.fieldValuesExtraction("d"),
    joiner = TransformerFactory.sumNumeric()
)
```

```
jsonMerge.getJsons() => list of one element: 16
```
