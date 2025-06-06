package com.jwcomptech.commons.javafx.resources;

/*-
 * #%L
 * JWCT Commons
 * %%
 * Copyright (C) 2025 JWCompTech
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.jwcomptech.commons.javafx.resources.enums.ResourceType;
import com.jwcomptech.commons.tuples.ImmutablePair;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Objects;

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;

/**
 * An object representation of an internal resource file in the
 * project resource directory.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Resource {
    ResourceType type;
    String filename;
    String fullName;

    @Contract(pure = true)
    public Resource(@NotNull final ResourceType type, final String filename) {
        checkArgumentNotNull(type, cannotBeNull("type"));
        checkArgumentNotNullOrEmpty(filename, "filename");
        this.type = type;
        this.filename = filename;

        //noinspection HardcodedFileSeparator
        fullName = type.getPath() + "/" + filename;
    }

    /**
     * Returns the resource path location as a URL object.
     * @return the resource path location as a URL object
     */
    public URL getURL() {
        return Objects.requireNonNull(getClass().getResource(fullName));
    }

    /**
     * Returns the resource path location as a String.
     * @return the resource path location as a String
     */
    public String getURLString() {
        return Objects.requireNonNull(getClass().getResource(fullName)).toString();
    }

    /**
     * Returns the resource as a JavaFX Image object.
     * @return the resource as a JavaFX Image object
     */
    public Image getAsImage() {
        return new Image(getURLString());
    }

    /**
     * Creates a Dialog object from the resource and returns the Dialog and FXMLLoader object.
     * @param <R> the class type to be returned when the dialog is closed
     * @return the new Dialog object and the FXMLLoader that was used to load the resource
     */
    @SneakyThrows
    public <R> ImmutablePair<FXMLLoader, Dialog<R>> getAsDialog() {
        final FXMLLoader loader = new FXMLLoader(getURL());
        final Dialog<R> dialog = loader.load();

        return new ImmutablePair<>(loader, dialog);
    }
}
