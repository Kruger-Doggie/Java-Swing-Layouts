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

/**
 * Enumeration representing layout constants for alignment and size in a user
 * interface. These constants define alignment and size specifications for
 * components in a layout.
 *
 * @author Kruger-Doggie
 */
public enum LayoutConstants {
    /**
     * Specifies alignment to the top.
     * <br>For use in vertical alignment.
     */
    ALIGNMENT_TOP,
    /**
     * Specifies alignment to the bottom.
     * <br>For use in vertical alignment.
     */
    ALIGNMENT_BOTTOM,
    /**
     * Specifies left alignment.
     * <br>For use in horizontal alignment.
     */
    ALIGNMENT_LEFT,
    /**
     * Specifies right alignment.
     * <br>For use in horizontal alignment.
     */
    ALIGNMENT_RIGHT,
    /**
     * Specifies both horizontal and vertical center alignment.
     */
    ALIGNMENT_CENTER,
    /**
     * Each component receives its preferred size.
     */
    SIZE_COMPONENT_PREFERRED,
    /**
     * Depending on the layout, either the maximum width or height (specified by
     * the layout) is used for all components. The other dimension corresponds
     * to the component's preferred size.
     */
    SIZE_MAX_COMPONENT_PREFERRED,
    /**
     * Depending on the layout, either the maximum available width or height is
     * used for all components. The other dimension corresponds to the
     * component's preferred size.
     */
    SIZE_MAX_AVAILABLE;

    /**
     * Checks if the given LayoutConstants value represents horizontal
     * alignment.
     *
     * @param value The LayoutConstants value to check.
     * @return {@code true} if the value is ALIGNMENT_LEFT, ALIGNMENT_CENTER, or
     * ALIGNMENT_RIGHT; {@code false} otherwise.
     */
    public static boolean isHorizontalAlignment(LayoutConstants value) {
        return value == ALIGNMENT_LEFT || value == ALIGNMENT_CENTER || value == ALIGNMENT_RIGHT;
    }

    /**
     * Checks if the given LayoutConstants value represents vertical alignment.
     *
     * @param value The LayoutConstants value to check.
     * @return {@code true} if the value is ALIGNMENT_TOP, ALIGNMENT_CENTER, or
     * ALIGNMENT_BOTTOM; {@code false} otherwise.
     */
    public static boolean isVerticallAlignment(LayoutConstants value) {
        return value == ALIGNMENT_TOP || value == ALIGNMENT_CENTER || value == ALIGNMENT_BOTTOM;
    }

    /**
     * Checks if the given LayoutConstants value represents a valid size value.
     *
     * @param value The LayoutConstants value to check.
     * @return {@code true} if the value is SIZE_COMPONENT_PREFERRED,
     * SIZE_MAX_COMPONENT_PREFERRED, or SIZE_MAX_AVAILABLE; {@code false}
     * otherwise.
     */
    public static boolean isSizeValue(LayoutConstants value) {
        return value == SIZE_COMPONENT_PREFERRED || value == SIZE_MAX_COMPONENT_PREFERRED || value == SIZE_MAX_AVAILABLE;
    }
}
