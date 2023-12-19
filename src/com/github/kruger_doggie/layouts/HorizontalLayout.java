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
 * A layout inspired by {@code FlowLayout} in horizontal form. Components are
 * arranged in a single row. The size mode affects only the height of the
 * components.
 *
 * @author Kruger-Doggie
 */
public class HorizontalLayout implements LayoutManager {

    /**
     * Horizontal gap between components.
     */
    private final int HGEP;
    /**
     * Mode for the height of the components.
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
     * @param sizeMode The mode to determine the height of the components.
     * @param hGep Horizontal gap between components.
     */
    public HorizontalLayout(LayoutConstants alignmentHorizontal, LayoutConstants alignmentVertical, LayoutConstants sizeMode, int hGep) {
        HGEP = Math.max(0, hGep);
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
                    width += preferredSize.width;
                    height = Math.max(height, preferredSize.height);
                }
            }
            if (visableCount > 1) {
                width += (visableCount - 1) * HGEP;
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
            //Case SIZE_MAX_AVAILABLE, ignore vertical alignment
            if (SIZEMODE == LayoutConstants.SIZE_MAX_AVAILABLE) {
                if (ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_LEFT) {
                    alignMaxAvailableLeft(parent);
                } else if (ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_CENTER) {
                    alignMaxAvailableCenter(parent);
                } else {//ALIGNMENT_HORIZONTAL == LayoutConstants.ALIGNMENT_RIGHT
                    alignMaxAvailableRight(parent);
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
     * Layout components aligned to the left for maximum available height.
     *
     * @param parent The container with the layout.
     */
    private void alignMaxAvailableLeft(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = insets.left;
        int y = insets.top;
        int height = parent.getHeight() - insets.top - insets.bottom;
        for (Component c : comps) {
            if (!c.isVisible()) {
                continue;
            }
            Dimension pref = c.getPreferredSize();
            c.setBounds(x, y, pref.width, height);
            x += HGEP + pref.width;
        }
    }

    /**
     * Layout components aligned to the center for maximum available height.
     *
     * @param parent The container with the layout.
     */
    private void alignMaxAvailableCenter(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int placedWidth = getComponentFullPlaceWidth(comps);
        int x = (parent.getWidth() - insets.left - insets.right) / 2 - placedWidth / 2 + insets.left;
        int y = insets.top;
        int height = parent.getHeight() - insets.top - insets.bottom;
        for (Component c : comps) {
            if (!c.isVisible()) {
                continue;
            }
            Dimension pref = c.getPreferredSize();
            c.setBounds(x, y, pref.width, height);
            x += HGEP + pref.width;
        }
    }

    /**
     * Layout components aligned to the right for maximum available height.
     *
     * @param parent The container with the layout.
     */
    private void alignMaxAvailableRight(Container parent) {
        Insets insets = parent.getInsets();
        Component[] comps = parent.getComponents();
        int x = parent.getWidth() - insets.right;
        int y = insets.top;
        int height = parent.getHeight() - insets.top - insets.bottom;
        for (int i = comps.length - 1; i >= 0; i--) {
            Component c = comps[i];
            if (!c.isVisible()) {
                continue;
            }
            Dimension pref = c.getPreferredSize();
            x -= pref.width;
            c.setBounds(x, y, pref.width, height);
            x -= HGEP;
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
                x += HGEP + pref.width;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int height = getMaxComponentPreferredHeight(comps);
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, pref.width, height);
                x += HGEP + pref.width;
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
        int x = insets.left;
        int y = (parent.getHeight() - insets.top - insets.bottom) / 2 + insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y - (pref.height / 2), pref.width, pref.height);
                x += HGEP + pref.width;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int height = getMaxComponentPreferredHeight(comps);
            y -= height / 2;
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, pref.width, height);
                x += HGEP + pref.width;
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
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y - pref.height, pref.width, pref.height);
                x += HGEP + pref.width;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int height = getMaxComponentPreferredHeight(comps);
            y -= height;
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, pref.width, height);
                x += HGEP + pref.width;
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
        int placedWidth = getComponentFullPlaceWidth(comps);
        int x = (parent.getWidth() - insets.left - insets.right) / 2 - placedWidth / 2 + insets.left;//(X Mittig im Plazierungsbereich) - (Halbe Breite der zu plazierenden Komponenten)
        int y = insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, pref.width, pref.height);
                x += HGEP + pref.width;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int height = getMaxComponentPreferredHeight(comps);
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, pref.width, height);
                x += HGEP + pref.width;
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
        int placedWidth = getComponentFullPlaceWidth(comps);
        int x = (parent.getWidth() - insets.left - insets.right) / 2 - placedWidth / 2 + insets.left;
        int y = (parent.getHeight() - insets.top - insets.bottom) / 2 + insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y - pref.height / 2, pref.width, pref.height);
                x += HGEP + pref.width;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int height = getMaxComponentPreferredHeight(comps);
            y -= height / 2;
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, pref.width, height);
                x += HGEP + pref.width;
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
        int placedWidth = getComponentFullPlaceWidth(comps);
        int x = (parent.getWidth() - insets.left - insets.right) / 2 - placedWidth / 2 + insets.left;
        int y = parent.getHeight() - insets.bottom;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y - pref.height, pref.width, pref.height);
                x += HGEP + pref.width;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int height = getMaxComponentPreferredHeight(comps);
            y -= height;
            for (Component c : comps) {
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                c.setBounds(x, y, pref.width, height);
                x += HGEP + pref.width;
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
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                x -= pref.width;
                c.setBounds(x, y, pref.width, pref.height);
                x -= HGEP;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int height = getMaxComponentPreferredHeight(comps);
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                x -= pref.width;
                c.setBounds(x, y, pref.width, height);
                x -= HGEP;
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
        int x = parent.getWidth() - insets.right;
        int y = (parent.getHeight() - insets.top - insets.bottom) / 2 + insets.top;
        if (SIZEMODE == LayoutConstants.SIZE_COMPONENT_PREFERRED) {
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                x -= pref.width;
                c.setBounds(x, y - pref.height / 2, pref.width, pref.height);
                x -= HGEP;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int height = getMaxComponentPreferredHeight(comps);
            y -= height / 2;
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                x -= pref.width;
                c.setBounds(x, y, pref.width, height);
                x -= HGEP;
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
                x -= pref.width;
                c.setBounds(x, y - pref.height, pref.width, pref.height);
                x -= HGEP;
            }
        } else {//SIZEMODE == LayoutConstants.SIZE_MAX_COMPONENT_PREFERRED
            int height = getMaxComponentPreferredHeight(comps);
            y -= height;
            for (int i = comps.length - 1; i >= 0; i--) {
                Component c = comps[i];
                if (!c.isVisible()) {
                    continue;
                }
                Dimension pref = c.getPreferredSize();
                x -= pref.width;
                c.setBounds(x, y, pref.width, height);
                x -= HGEP;
            }
        }
    }

    /**
     * Utility method to determine the maximum preferred height among all
     * visible components.
     *
     * @param comps An array of components.
     * @return The maximum preferred height among visible components.
     */
    private int getMaxComponentPreferredHeight(Component[] comps) {
        int height = 0;
        for (Component c : comps) {
            if (c.isVisible()) {
                Dimension pref = c.getPreferredSize();
                if (height < pref.height) {
                    height = pref.height;
                }
            }
        }
        return height;
    }

    /**
     * Utility method to calculate the width required by components with gaps
     * between them.
     *
     * @param comps An array of components.
     * @return The total width required by visible components, accounting for
     * gaps.
     */
    private int getComponentFullPlaceWidth(Component[] comps) {
        int width = 0;
        int visableCount = 0;
        for (Component c : comps) {
            if (c.isVisible()) {
                visableCount++;
                Dimension preferredSize = c.getPreferredSize();
                width += preferredSize.width;
            }
        }
        if (visableCount > 1) {
            width += (visableCount - 1) * HGEP;
        }
        return width;
    }

    /**
     * Get the string representation of the HorizontalLayout.
     *
     * @return A string representation of the HorizontalLayout.
     */
    @Override
    public String toString() {
        return "HorizontalLayout[horizontalAlignment=\"%s\";verticalAlignment=\"%s\";sizeMode=\"%s\";hGep=%d]".formatted(ALIGNMENT_HORIZONTAL, ALIGNMENT_VERTICAL, SIZEMODE, HGEP);
    }
}
