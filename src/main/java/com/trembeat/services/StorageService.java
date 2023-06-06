package com.trembeat.services;

import java.io.*;

/**
 * General filesystem content storage and retrieval service
 * @param <TID> Entity identifier type
 */
public abstract class StorageService<TID> {
    /**
     * Get absolute filepath for given id
     * @param id Id of associated entity to retrieve absolute filepath for
     * @return absolute filepath for given entity id
     */
    protected abstract String getFullPath(TID id);

    /**
     * Get file extension for given id
     * @param id Id of associated entity to retreive file extension for
     * @return Corresponding file extension
     */
    protected abstract String getFileExtension(TID id);

    /**
     * Check if given content type is accepted by storage service
     * @param contentType Content type to check
     * @return true, if content type is accepted, otherwise - false
     */
    public abstract boolean isAcceptedContentType(String contentType);

    /**
     * Get preferred content type for storing content
     * @return String representing preferred content type
     */
    public abstract String getPreferredContentType();

    /**
     * Save data for given entity id
     * @param id Id of associated entity to save
     * @param inputStream Incoming data stream
     * @return true, if file was saved successfully, otherwise - false
     */
    public boolean save(TID id, InputStream inputStream) {
        File file = new File(getFullPath(id));

        try {
            file.getParentFile().mkdirs();
            if (!file.exists())
                file.createNewFile();

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            inputStream.transferTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Load data for given entity id
     * @param id Id of associated entity to load
     * @return Open data input stream
     */
    public InputStream load(TID id) {
        try {
            File file = new File(getFullPath(id));
            if (!file.exists())
                throw new FileNotFoundException();

            return new BufferedInputStream(new FileInputStream(file));
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Delete data for given entity id
     * @param id Id of associated entity to delete
     * @return true, if data was deleted successfully, otherwise - false
     */
    public boolean delete(TID id) {
        try {
            File file = new File(getFullPath(id));
            if (!file.exists())
                throw new FileNotFoundException();

            file.delete();
        } catch (Exception ex) {
            return false;
        }

        return true;
    }
}
