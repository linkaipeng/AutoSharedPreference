
[中文文档](https://github.com/linkaipeng/AutoSharedPreference/blob/master/README_zh.md)


# AutoSharedPreference

`AutoSharedPreference` is an automatic generation of `SharedPreference` by adding **Annotations** to class.


### Usage

#### build.gradle

```
implementation 'me.linkaipeng:autoSPAnnotation:1.0.0'
annotationProcessor 'me.linkaipeng:autoSPCompiler:1.0.0'
```

### Initialize

```
AutoSharedPreferenceConfig.getInstance().init(this);
```

#### Add annotations to the class

```
@AutoSharedPreferences(mode = Context.MODE_PRIVATE)
public class AppConfig {

    @AutoGenerateField(filedName = "StudentName", defaultStringValue = "ddd", commitType = AutoGenerateField.CommitType.APPLY)
    private String name;

    @AutoGenerateField(defaultIntValue = -10)
    private int count;

    @AutoGenerateField(defaultLongValue = 90l, commitType = AutoGenerateField.CommitType.COMMIT)
    private long startTime;

    @AutoGenerateField(defaultBooleanValue = true)
    private boolean isClose;

    @AutoGenerateField(defaultFloatValue = 0.534534534f)
    private float price;

    @AutoGenerateField
    private Set<String> productSet;
}

```

- `mode` can use the value in `Context`；
- If you want to customize the `SharedPreferences` filename, you can use the `name` attribute.。

#### Add annotations to the field

```
@AutoGenerateField(filedName = "StudentName", defaultStringValue = "ddd", commitType = AutoGenerateField.CommitType.APPLY)
private String name;

```

- `filedName` is used to define the name of `key` stored in `SharedPreferences`. The default value is the field name；
- `defaultStringValue`, `defaultIntValue` are used to set default values；
- `commitType` is the save mode, you can choose `commit` or `apply`, the default value is `commit`.


#### You can view the generated code in `app/build/generated`

```
public final class AppConfigSP {
  private static AppConfigSP sInstance;

  private SharedPreferences mSharedPreferences;

  private AppConfigSP() {
    mSharedPreferences = AutoSharedPreferenceConfig.getInstance().getContext().getSharedPreferences("AppConfigSP", 0);
  }

  public static AppConfigSP getInstance() {
    if (null == sInstance) {
        sInstance = new AppConfigSP();
        }
        return sInstance;
  }

  public SharedPreferences getSharedPreferences() {
    return mSharedPreferences;
  }

  public boolean contains(String key) {
    return mSharedPreferences.contains(key);
  }

  public String getName() {
    return mSharedPreferences.getString("StudentName", "ddd");
  }

  public void setName(String value) {
    mSharedPreferences.edit().putString("StudentName", value).apply();
  }
}

```
