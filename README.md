[![](https://jitpack.io/v/appwise-labs/measurements.svg)](https://jitpack.io/#appwise-labs/measurements)

# Measurements

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Usage](#usage)

---

## Gradle Dependency

To add this library to your project, you'll have to add the following dependency.

```groovy
dependencies {
  ...
  implementation 'com.github.appwise-labs:measurements:<Latest-Version>'
}
```

---

## Usage

Following line can be used to define a Measurement with a Unit of kilograms.

```kotlin
val m1 = Measurement(0.2, UnitMass.kilograms)
```

In order to convert it to another unit (of the same unit type)

```kotlin
val m2 = m1.converted(UnitMass.pounds)
```

When you want to format the measurement to show it to the user you can call the `format` function.

```kotlin
println(m1.format())
```

You can also make calculations with different measurements

```kotlin
val m1 = Measurement(0.2, UnitMass.kilograms)
val m2 = m1.converted(UnitMass.pounds)

val sum = m1 + m2
println(sum.format())
```

Which would result in "0.4 kg".

---
