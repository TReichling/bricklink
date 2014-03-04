/*
 * GPLv3
 */

package org.kleini.bricklink.api;

/**
 * {@link ColorsRequest}
 *
 * @author <a href="mailto:himself@kleini.org">Marcus Klein</a>
 */
public final class ColorsRequest implements Request<ColorsResponse> {

    public ColorsRequest() {
        super();
    }

    @Override
    public String getPath() {
        return "colors";
    }

    @Override
    public Parameter[] getParameters() {
        return Parameter.EMPTY;
    }

    @Override
    public ColorsParser getParser() {
        return new ColorsParser();
    }
}