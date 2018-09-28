//Autogenerated
package com.vision4j.completion;

import com.google.protobuf.ByteString;
import com.vision4j.completion.grpc.CompletionInput;
import com.vision4j.utils.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.vision4j.completion.grpc.CompletionGrpc;
import com.vision4j.completion.grpc.Image;
import com.vision4j.completion.grpc.CompletedImage;

import javax.imageio.ImageIO;

public class GrpcCompletion implements Completion {

    private final Categories categories;

    private final CompletionGrpc.CompletionBlockingStub completionStub;

    public GrpcCompletion(Categories categories, ManagedChannel channel) {
        this.categories = categories;
        this.completionStub = CompletionGrpc.newBlockingStub(channel);
    }

    public GrpcCompletion(String[] categoriesArray, ManagedChannel channel) {
        this.categories = new Categories(categoriesArray);
        this.completionStub = CompletionGrpc.newBlockingStub(channel);
    }

    public GrpcCompletion(String[] categoriesArray, String host, int port) {
        this(categoriesArray, ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    public GrpcCompletion(String[] categoriesArray) {
        this(categoriesArray, "localhost", 50053);
    }

    @Override()
    public CompletionResult complete(InputStream image, InputStream mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(InputStream image, File mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(InputStream image, byte[] mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), mask);
    }

    @Override()
    public CompletionResult complete(InputStream image, URL mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(File image, InputStream mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(File image, File mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(File image, byte[] mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), mask);
    }

    @Override()
    public CompletionResult complete(File image, URL mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(byte[] image, InputStream mask) throws IOException {
        return this.complete(image, VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(byte[] image, File mask) throws IOException {
        return this.complete(image, VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(byte[] image, byte[] mask) throws IOException {
        Image serializedimage = this.prepareGrpcBytes(image);
        Image serializedmask = this.prepareGrpcBytes(mask);
        CompletionInput.Builder completionInputBuilder = CompletionInput.newBuilder();
        completionInputBuilder.setImage(serializedimage);
        completionInputBuilder.setMask(serializedmask);
        CompletionInput completionInput = completionInputBuilder.build();
        CompletedImage completedimage = completionStub.complete(completionInput);
        return this.convert(completedimage);
    }

    @Override()
    public CompletionResult complete(byte[] image, URL mask) throws IOException {
        return this.complete(image, VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(URL image, InputStream mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(URL image, File mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), VisionUtils.toByteArray(mask));
    }

    @Override()
    public CompletionResult complete(URL image, byte[] mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), mask);
    }

    @Override()
    public CompletionResult complete(URL image, URL mask) throws IOException {
        return this.complete(VisionUtils.toByteArray(image), VisionUtils.toByteArray(mask));
    }

    private CompletionResult convert(CompletedImage completedimage) throws IOException {
        byte[] result = completedimage.getResult().toByteArray();
        BufferedImage resultImage = ImageIO.read(new ByteArrayInputStream(result));
        return new CompletionResult(resultImage);
    }

    private Image prepareGrpcBytes(byte[] image) throws IOException {
        SimpleImageInfo simpleImageInfo = new SimpleImageInfo(image);
        ByteString imageData = ByteString.copyFrom(image);
        Image.Builder imageBuilder = Image.newBuilder();
        imageBuilder.setWidth(simpleImageInfo.getWidth());
        imageBuilder.setHeight(simpleImageInfo.getHeight());
        imageBuilder.setChannels(3);
        imageBuilder.setImageData(imageData);
        return imageBuilder.build();
    }
}