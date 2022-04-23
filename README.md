# manson
A Json Library in Kotlin to perform high-level transformations.

JsonPath object transformation :

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
