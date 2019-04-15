# AutoSharedPreference

`AutoSharedPreference` 是一个可以通过给类成员添加**注解**来自动生成 `SharedPreference`。



### 使用方式

#### module 的 build.gradle 使用

```
implementation 'me.linkaipeng:autoSPAnnotation:1.0.0'
annotationProcessor 'me.linkaipeng:autoSPCompiler:1.0.0'
```

### 初始化

```
AutoSharedPreferenceConfig.getInstance().init(this);
```

#### 类添加注解

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

- `@AutoSharedPreferences` 可以添加`mode`，`mode` 的值跟原生写法一样，用 `Context` 中的值；
- 如果你想自定义 `SharedPreferences` 文件名，可以用 `name` 属性。

#### 成员添加注解

```
@AutoGenerateField(filedName = "StudentName", defaultStringValue = "ddd", commitType = AutoGenerateField.CommitType.APPLY)
private String name;

```

- `filedName` 用于定义 `SharedPreferences` 中存储 `key` 的名字，默认值为变量名；
- `defaultStringValue`、 `defaultIntValue` 等为指定各种类型的默认值；
- `commitType` 为保存方式，有 `commit` 和 `apply`，默认值为 `commit`.


#### 生成代码可在 `app/build/generated` 路径下查看

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
