/*
 * GPLv3
 */

package org.kleini.bricklink.api;

/**
 * {@link ItemNumberRequest}
 *
 * @author <a href="mailto:himself@kleini.org">Marcus Klein</a>
 */
public class ItemNumberRequest implements Request<ItemNumberResponse> {

    private final int elementId;

    public ItemNumberRequest(int elementId) {
        super();
        this.elementId = elementId;
    }

    @Override
    public String getPath() {
        return "item_mapping/" + Integer.toString(elementId);
    }

    @Override
    public Parameter[] getParameters() {
        return Parameter.EMPTY;
    }

    @Override
    public ItemNumberParser getParser() {
        return new ItemNumberParser();
    }

}
