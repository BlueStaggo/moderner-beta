package mod.bluestaggo.modernerbeta.client.gui.widget;

//? if >=26.1 {
/*import mod.bluestaggo.modernerbeta.mixin.client.EditBoxAccessor;
import net.minecraft.util.StringUtil;
*///? }
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public class FilteredEditBox extends EditBox {
    //? if >=26.1 {
    /*/^*
     * Called to check if the text is valid
     ^/
    private Predicate<String> filter = Objects::nonNull;
    *///? }

    public FilteredEditBox(Font font, int width, int height, Component narration) {
        super(font, 0, 0, width, height, narration);
    }

    public FilteredEditBox(Font font, int x, int y, int width, int height, Component narration) {
        super(font, x, y, width, height, narration);
    }

    public FilteredEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox oldBox, Component narration) {
        super(font, x, y, width, height, oldBox, narration);
    }

    //? if >=26.1 {
    /*@Override
    public void setValue(final String text) {
        if (this.filter.test(text)) {
            super.setValue(text);
        }
    }

    public void setFilter(Predicate<String> validator) {
        this.filter = validator;
    }

    @Override
    public void insertText(final String input) {
        int cursorPos = this.getCursorPosition();
        String value = this.getValue();

        EditBoxAccessor accessor = (EditBoxAccessor) this;
        int highlightPos = accessor.getHighlightPos();

        int start = Math.min(cursorPos, highlightPos);
        int end = Math.max(cursorPos, highlightPos);
        int maxInsertionLength = accessor.getMaxLength() - value.length() - (start - end);
        if (maxInsertionLength > 0) {
            String text = StringUtil.filterText(input);
            int insertionLength = text.length();
            if (maxInsertionLength < insertionLength) {
                if (Character.isHighSurrogate(text.charAt(maxInsertionLength - 1))) {
                    maxInsertionLength--;
                }

                text = text.substring(0, maxInsertionLength);
                insertionLength = maxInsertionLength;
            }

            String updated = new StringBuilder(value).replace(start, end, text).toString();
            if (this.filter.test(updated)) {
                accessor.setValue(updated);
                int newCursorPos = start + insertionLength;
                this.setCursorPosition(newCursorPos);
                this.setHighlightPos(newCursorPos);
                accessor.invokeOnValueChange(updated);
            }
        }
    }

    @Override
    public void deleteCharsToPos(final int pos) {
        String value = this.getValue();
        EditBoxAccessor accessor = (EditBoxAccessor) this;

        if (!value.isEmpty()) {
            int cursorPos = this.getCursorPosition();

            if (accessor.getHighlightPos() != cursorPos) {
                this.insertText("");
            } else {
                int start = Math.min(pos, cursorPos);
                int end = Math.max(pos, cursorPos);
                if (start != end) {
                    String updated = new StringBuilder(value).delete(start, end).toString();
                    if (this.filter.test(updated)) {
                        accessor.setValue(updated);
                        this.setCursorPosition(start);
                        accessor.invokeOnValueChange(updated);
                        this.moveCursorTo(start, false);
                    }
                }
            }
        }
    }
    *///? }
}
