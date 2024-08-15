    package com.outsider.masterofpredictionbackend.utils;

    import org.springframework.web.multipart.MultipartFile;

    import java.io.File;
    import java.io.IOException;
    import java.io.UncheckedIOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;
    import java.util.stream.Collectors;

    public class ImageStorageService {

        private static final char separator = '_';

        // 임시 이미지 저장 경로
        private static final String uploadDir = "src/main/resources/static/images/";

        public static String saveImage(MultipartFile imageFile) throws IOException {
            if (imageFile != null){

                StringBuilder sb = new StringBuilder();

                sb.append(UUID.randomUUID());
                // uuid와 실제 파일이름 분리용문자
                sb.append(separator);
                sb.append(imageFile.getOriginalFilename());


                Path filePath = Paths.get(uploadDir + sb);
                Files.createDirectories(filePath.getParent()); // 디렉토리가 없을 경우 생성
                Files.write(filePath, imageFile.getBytes()); // 파일 저장
                return sb.toString();
            }
            return null;
        }

        public static void deleteImage(String fileName) {
            File file = new File(uploadDir + fileName);
            if (file.exists()) {
                file.delete();
            }
        }


        /**
         * 이미지 파일을 저장하고, 성공적으로 저장된 이미지 파일 이름을 리스트로 반환
         * @param files 저장할 이미지 파일 리스트
         * @return 저장된 이미지 파일 이름 리스트
         * @throws RuntimeException 이미지 파일 저장 실패시 저장된 이미지 파일 삭제 후 발생
         */
        public static List<String> saveAndReturnImageNames(List<MultipartFile> files) {
            List<String> savedImageNames = new ArrayList<>();
            try {
                // 이미지 파일을 저장하고, 성공적으로 저장된 이미지 이름을 리스트에 추가
                if (files == null || files.isEmpty()){
                    return savedImageNames;
                }
                for (MultipartFile file : files) {
                    try {
                        String savedImageName = saveImage(file);
                        savedImageNames.add(savedImageName);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            } catch (UncheckedIOException e) {
                deleteImages(savedImageNames);
                throw new InvalidImageException("Failed to save images");
            }

            return savedImageNames;
        }

        private static void deleteImages(List<String> imageNames) {
            imageNames.forEach(ImageStorageService::deleteImage);
        }
    }
