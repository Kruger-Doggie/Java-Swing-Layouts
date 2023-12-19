/*
MIT License

Copyright (c) 2023 Kruger-Doggie

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.github.kruger_doggie.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * A layout inspired by {@code FlowLayout} in vertical form. Components are
 * arranged in a single column. The size mode affects only the width of the
 * components.
 *
 * @author Kruger-Doggie
 */
public class VerticalLayout implements LayoutManager {

    /**
     * Vertical gap between components.
     */
    private final int VGEP;
    /**
     * Mode for the width of the components.
     */
    private final LayoutConstants SIZEMODE;
    /**
     * Alignment for the vertical axis.
     */
    private final LayoutConstants ALIGNMENT_VERTICAL;
    /**
     * Alignment for the horizontal axis.
     */
    private final LayoutConstants ALIGNMENT_HORIZONTAL;

    /**
     * Creates a new layout.
     *
     * @param alignmentHorizontal The alignment of components in the horizontal
     * direction.
     * @param alignmentVertical The alignment of components in the vertical
     * direction.
     * @param sizeMode The mode to determine the width of the components.
     * @param vGep Vertical gap between components.
     */
    public VerticalLayout(LayoutConstants alignmentHorizontal, LayoutConstants alignmentVertical, LayoutConstants sizeMode, int vGep) {
        VGEP = Math.max(0, vGep);
        if (!LayoutConstants.isHorizontalAlignment(alignmentHorizontal)) {
            throw new IllegalArgumentException("Unsupported value passed for alignmentHorizontal!");
        }
        if (!LayoutConstants.isVerticallAlignment(alignmentVertical)) {
            throw new IllegalArgumentException("Unsupported value passed for alignmentVertical!");
        }
        if (!LayoutConstants.isSizeValue(sizeMode)) {
            throw new IllegalArgumentException("Unsupported value passed for sizeMode!");
        }
        SIZEMODE = sizeMode;
        ALIGNMENT_VERTICAL = alignmentVertical;
        ALIGNMENT_HORIZONTAL = alignmentHorizontal;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int width = 0;
        int height = 0;
        synchronized (parent.getTreeLock()) {
            int visableCount = 0;
            for (Component c : parent.getComponents()) {
                if (c.isVisible()) {
                    visableCount++;
                    Dimension preferredSize = c.getPreferredSize();
                    width = Math.max(width, preferredSize.width);
                    height += preferredSize.height;
                }
            }
            if (visableCount > 1) {
                height += (visableCount - 1) * VGEP;
            }
            Insets insets = parent.getInsets();
            width += insets.left + insets.right;
            height += insets.top + insets.bottom;
        }
        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            //Case SIZE_MAX_AVAILABLE, ignore horizontal alignment
            if (SIZEMODE == LayoutConstants.SIZE_MAX_AVAILABLE) {
                if (ALIGNMENT_VERTICAL == LayoutConstants.ALIGNMENT_TOP) {
                    alignMaxAvailableTop(parent);
                } else if (ALIGNMENT_VERTICAL == LayoutConstants.ALIGNMENT_CENTER) {
                    alignMaxAvailableCenter(parent);
                } else {//ALIGNMENT_VERTICAL == LayoutConstants.ALIGNMENT_BOTTOM
                    alignMaxAvailableBottom(parent);
                }
            }//
            //Use both alignments
            else if (ALIGNMENT_VERTICAL == LayoutConstants.ALIGNMENT_TOP) {
                if (ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_LEFT) {
                    alignTopLeft(parent);
                } else if (ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_CENTER) {
                    alignTopCenter(parent);
                } else {//ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_RIGHT
                    alignTopRight(parent);
                }
            } else if (ALIGNMENT_VERTICAL == LayoutConstants.ALIGNMENT_CENTER) {
                if (ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_LEFT) {
                    alignCenterLeft(parent);
                } else if (ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_CENTER) {
                    alignCenterCenter(parent);
                } else {//ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_RIGHT
                    alignCenterRight(parent);
                }
            } else {//ALIGNMENT_VERTICAL == LayoutConstants.ALIGNMENT_BOTTOM
                if (ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_LEFT) {
                    alignBottomLeft(parent);
                } else if (ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_CENTER) {
                    alignBottomCenter(parent);
                } else {//ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_RIGHT
                    alignBottomRight(parent);
                }
            }
        }
    }

    /**
     * Layout components aligned to the top for maximum available width.
     *
     * @param parent The container with the layout.
     */
    private void alignMaxAvailableTop(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = insets.left;
        int y = insets.top;
        int width = parent.getWidth() - insets.left - insets.right;
        for (Component c : comps) {
            if (!c.isVisible()) {
                continue;
            }
            Dimension pref = c.getPreferredSize();
            c.setBounds(x, y, width, pref.height);
            y += VGEP + pref.height;
        }
    }

    /**
     * Layout components aligned to the center for maximum available width.
     *
     * @param parent The container with the layout.
     */
    private void alignMaxAvailableCenter(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int placedHeight = getComponentFullPlaceHeight(comps);
        int x = insets.left;
        int y = (parent.getHeight() - insets.top - insets.bottom) / 2 - placedHeight / 2 + insets.top;
        int width = parent.getWidth() - insets.left - insets.right;
        for (Component c : comps) {
            if (!c.isVisible()) {
                continue;
            }
            Dimension pref = c.getPreferredSize();
            c.setBounds(x, y, width, pref.height);
            y += VGEP + pref.height;
        }
    }

    /**
     * Layout components aligned to the bottom for maximum available width.
     *
     * @param parent The container with the layout.
     */
    private void alignMaxAvailableBottom(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = insets.left;
        int y = parent.getHeight() - insets.bottom;
        int width = parent.getWidth() - insets.left - insets.right;
        for (int i = comps.length - 1; i >= 0; i--) {
            Component c = comps[i];
            if (!c.isVisible()) {
                continue;
            }
            Dimension pref = c.getPreferredSize();
            y -= pref.height;
            c.setBounds(x, y, width, pref.height);
            y -= VGEP;
        }
    }

    /**
     * Align components at the top-left corner.
     *
     * @param parent The container with the layout.
     */
    private void alignTopLeft(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = insets.left;
        int y = insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, pref.width, pref.height);
                y += VGEP + pref.height;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int width = getMaxComponentWidth(comps);
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, width, pref.height);
                y += VGEP + pref.height;
            }
        }
    }

    /**
     * Align components at the center-left.
     *
     * @param parent The container with the layout.
     */
    private void alignCenterLeft(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int placedHeight = getComponentFullPlaceHeight(comps);
        int x = insets.left;
        int y = (parent.getHeight() - insets.top - insets.bottom) / 2 - placedHeight / 2 + insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, pref.width, pref.height);
                y += VGEP + pref.height;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int width = getMaxComponentWidth(comps);
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, width, pref.height);
                y += VGEP + pref.height;
            }
        }
    }

    /**
     * Align components at the bottom-left corner.
     *
     * @param parent The container with the layout.
     */
    private void alignBottomLeft(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = insets.left;
        int y = parent.getHeight() - insets.bottom;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y - pref.height, pref.width, pref.height);
                y -= VGEP + pref.height;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int width = getMaxComponentWidth(comps);
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                y -= pref.height;
                c.setBounds(x, y, width, pref.height);
                y -= VGEP;
            }
        }
    }

    /**
     * Align components at the top-center.
     *
     * @param parent The container with the layout.
     */
    private void alignTopCenter(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = (parent.getWidth() - insets.left - insets.right) / 2 + insets.left;
        int y = insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x - pref.width / 2, y, pref.width, pref.height);
                y += VGEP + pref.height;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int width = getMaxComponentWidth(comps);
            x -= width / 2;
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, width, pref.height);
                y += VGEP + pref.height;
            }
        }
    }

    /**
     * Align components at the center-center.
     *
     * @param parent The container with the layout.
     */
    private void alignCenterCenter(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int placedHeight = getComponentFullPlaceHeight(comps);
        int x = (parent.getWidth() - insets.left - insets.right) / 2 + insets.left;
        int y = (parent.getHeight() - insets.top - insets.bottom) / 2 - placedHeight / 2 + insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x - pref.width / 2, y, pref.width, pref.height);
                y += VGEP + pref.height;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int width = getMaxComponentWidth(comps);
            x -= width / 2;
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, width, pref.height);
                y += VGEP + pref.height;
            }
        }
    }

    /**
     * Align components at the bottom-center.
     *
     * @param parent The container with the layout.
     */
    private void alignBottomCenter(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = (parent.getWidth() - insets.left - insets.right) / 2 + insets.left;
        int y = parent.getHeight() - insets.bottom;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x - pref.width / 2, y - pref.height, pref.width, pref.height);
                y -= VGEP + pref.height;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int width = getMaxComponentWidth(comps);
            x -= width / 2;
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                y -= pref.height;
                c.setBounds(x, y, width, pref.height);
                y -= VGEP;
            }
        }
    }

    /**
     * Align components at the top-right corner.
     *
     * @param parent The container with the layout.
     */
    private void alignTopRight(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = parent.getWidth() - insets.right;
        int y = insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x - pref.width, y, pref.width, pref.height);
                y += VGEP + pref.height;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int width = getMaxComponentWidth(comps);
            x -= width;
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, width, pref.height);
                y += VGEP + pref.height;
            }
        }
    }

    /**
     * Align components at the center-right.
     *
     * @param parent The container with the layout.
     */
    private void alignCenterRight(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int placedHeight = getComponentFullPlaceHeight(comps);
        int x = parent.getWidth() - insets.right;
        int y = (parent.getHeight() - insets.top - insets.bottom) / 2 - placedHeight / 2 + insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x - pref.width, y, pref.width, pref.height);
                y += VGEP + pref.height;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int width = getMaxComponentWidth(comps);
            x -= width;
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, width, pref.height);
                y += VGEP + pref.height;
            }
        }
    }

    /**
     * Align components at the bottom-right corner.
     *
     * @param parent The container with the layout.
     */
    private void alignBottomRight(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = parent.getWidth() - insets.right;
        int y = parent.getHeight() - insets.bottom;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x - pref.width, y - pref.height, pref.width, pref.height);
                y -= VGEP + pref.height;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int width = getMaxComponentWidth(comps);
            x -= width;
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                y -= pref.height;
                c.setBounds(x, y, width, pref.height);
                y -= VGEP;
            }
        }
    }

    /**
     * Utility method to determine the maximum preferred width among all visible
     * components.
     *
     * @param comps An array of components.
     * @return The maximum preferred width among visible components.
     */
    private int getMaxComponentWidth(Component[] comps) {
        int width = 0;
        for (Component c : comps) {
            if (c.isVisible()) {
                Dimension pref = c.getPreferredSize();
                if (width < pref.width) {
                    width = pref.width;
                }
            }
        }
        return width;
    }

    /**
     * Utility method to calculate the height required by components with gaps
     * between them.
     *
     * @param comps An array of components.
     * @return The total height required by visible components, accounting for
     * gaps.
     */
    private int getComponentFullPlaceHeight(Component[] comps) {
        int height = 0;
        int visableCount = 0;
        for (Component c : comps) {
            if (c.isVisible()) {
                visableCount++;
                Dimension preferredSize = c.getPreferredSize();
                height += preferredSize.height;
            }
        }
        if (visableCount > 1) {
            height += (visableCount - 1) * VGEP;
        }
        return height;
    }

    @Override
    public String toString() {
        return "VerticalLayout[horizontalAlignment=\"%s\";verticalAlignment=\"%s\";sizeMode=\"%s\";hGep=%d]".formatted(ALIGNMENT_HORIZONTAL, ALIGNMENT_VERTICAL, SIZEMODE, VGEP);
    }
}
