# Java-Swing-Layouts
## VerticalLayout
VerticalLayout is a custom Java layout manager inspired by `FlowLayout` but designed for vertical arrangement. It allows you to easily organize Swing components in a single column with customizable alignment and sizing options.

### Features
- **Vertical Arrangement:** Components are arranged in a single column, creating a vertical layout.
- **Alignment Options:** Choose from various alignment options for both vertical and horizontal axes.
- **Size Modes:** Determine the width of components using different size modes.
- **Customizable Gaps:** Specify the vertical gap between components.

#### Sample Code
```java
VerticalLayout layout = new VerticalLayout(LayoutConstants.ALIGNMENT_CENTER, LayoutConstants.ALIGNMENT_TOP, LayoutConstants.SIZE_COMPONENT_PREFERRED, 5);
JPanel panel = new JPanel();
panel.setLayout(layout);
```

#### Size modifier example
![Size modifier example for HorizontalLayout](/Screenshots/Size%20example%20VerticalLayout.png)

## HorizontalLayout
HorizontalLayout is a custom Java layout manager inspired by `FlowLayout` but designed for horizontal arrangement. It allows you to easily organize Swing components in a single row with customizable alignment and sizing options.

### Features
- **Horizontal Arrangement:** Components are arranged in a single row, creating a horizontal layout.
- **Alignment Options:** Choose from various alignment options for both horizontal and vertical axes.
- **Size Modes:** Determine the height of components using different size modes.
- **Customizable Gaps:** Specify the horizontal gap between components.

#### Sample Code
```java
HorizontalLayout layout = new HorizontalLayout(LayoutConstants.ALIGNMENT_LEFT, LayoutConstants.ALIGNMENT_TOP, LayoutConstants.SIZE_COMPONENT_PREFERRED, 5);
JPanel panel = new JPanel();
panel.setLayout(layout);
```

#### Size modifier example
![Size modifier example for HorizontalLayout](/Screenshots/Size%20example%20HorizontalLayout.png)
