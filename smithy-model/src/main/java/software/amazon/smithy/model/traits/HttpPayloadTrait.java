/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.smithy.model.traits;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;

/**
 * Binds a single structure member to the payload of an HTTP request.
 */
public final class HttpPayloadTrait extends AnnotationTrait {
    public static final ShapeId ID = ShapeId.from("smithy.api#httpPayload");

    public HttpPayloadTrait(ObjectNode node) {
        super(ID, node);
    }

    public HttpPayloadTrait() {
        this(Node.objectNode());
    }

    public static final class Provider extends AnnotationTrait.Provider<HttpPayloadTrait> {
        public Provider() {
            super(ID, HttpPayloadTrait::new);
        }
    }
}
