# プロジェクト作成

```text
mvn archetype:generate -DgroupId=jp.co.baobhansith.server -DartifactId=baobhansith -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

## xmlサンプル

`dom4j`を利用して、以下のxmlを作成したいです。
ただし、以下の制約も考慮してください。

1. 要素に値がない場合、そのタグは出力しません
2. あるタグ配下の要素がない場合、親タグも出力しません

```xml
<Root>
    <Payload>
        <ParentTag>
            <ChildTag>
                <Enabled>true</Enabled>
                <EnabledDate>2024-09-01</EnabledDate>
            </ChildTag>
            <ChildTag>
                <Version>1.0.0</Version>
                <VersionName>Version-1.0.0</VersionName>
            </ChildTag>
        </ParentTag>
        <Tag2>
            <Names>
                <Name>
                    <Id>F000000001</Id>
                </Name>
            </Names>
        </Tag2>
    </Payload>
</Root>
```

--

以下のような繰り返し構造を出力できるようにしてください。

```xml
<Root>
    <Payload>
        <Info>
            <ParentTag>
                <ChildTag>
                    <Enabled>true</Enabled>
                    <EnabledDate>2024-09-01</EnabledDate>
                </ChildTag>
                <ChildTag>
                    <Version>1.0.0</Version>
                    <VersionName>Version-1.0.0</VersionName>
                </ChildTag>
            </ParentTag>
            <Tag2>
                <Names>
                    <Name>
                        <Id>F000000001</Id>
                    </Name>
                </Names>
            </Tag2>
        </Info>
        <Info>
            <ParentTag>
                <ChildTag>
                    <Enabled>true</Enabled>
                    <EnabledDate>2024-09-01</EnabledDate>
                </ChildTag>
                <ChildTag>
                    <Version>1.0.0</Version>
                    <VersionName>Version-1.0.0</VersionName>
                </ChildTag>
            </ParentTag>
            <Tag2>
                <Names>
                    <Name>
                        <Id>F000000002</Id>
                    </Name>
                </Names>
            </Tag2>
        </Info>
    </Payload>
</Root>
```