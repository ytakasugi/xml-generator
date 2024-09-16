package jp.co.baobhansith.server.util;

import java.util.ArrayList;
import java.util.List;

import jp.co.baobhansith.server.bean.Info;
import jp.co.baobhansith.server.bean.ChildTag;
import jp.co.baobhansith.server.bean.Name;
import jp.co.baobhansith.server.bean.Names;
import jp.co.baobhansith.server.bean.ParentTag;
import jp.co.baobhansith.server.bean.Tag2;

public class Parser {
    public static <T> List<T> generator(String[] csvArray, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (String csv : csvArray) {
            list.add(parse(csv, clazz));
        }
        return list;
    }

    private static <T> T parse(String csv, Class<T> clazz) {
        String[] values = csv.split(",");
        if (clazz == Info.class) {
            ChildTag childTag1 = new ChildTag();
            childTag1.setEnabled(values[0]);
            childTag1.setEnabledDate(values[1]);

            ChildTag childTag2 = new ChildTag();
            childTag2.setVersion(values[2]);
            childTag2.setVersionName(values[3]);

            ParentTag parentTag = new ParentTag();
            List<ChildTag> childTags = new ArrayList<>();
            childTags.add(childTag1);
            childTags.add(childTag2);
            parentTag.setChildTags(childTags);

            Name name = new Name();
            name.setId(values[4]);

            Names names = new Names();
            List<Name> nameList = new ArrayList<>();
            nameList.add(name);
            names.setNameList(nameList);

            Tag2 tag2 = new Tag2();
            tag2.setNames(names);

            Info info = new Info();
            info.setParentTag(parentTag);
            info.setTag2(tag2);

            return clazz.cast(info);
        }
        throw new IllegalArgumentException("Unsupported class: " + clazz);
    }
}
