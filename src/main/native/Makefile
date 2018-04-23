include ./Makefile.common

all: libmusket.so

musket:
	$(MAKE) -C $(MUSKET_DIR)
	if [ ! -d "$(BUILD_DIR)" ]; then $(MKDIR) $(BUILD_DIR); fi
	$(CP) $(MUSKET_DIR)/*.o $(BUILD_DIR)

libmusket.so: musket
	$(CC) $(CFLAGS) callMusket.c -o $(BUILD_DIR)/callMusket.o
	$(CXX) $(LIBMUSKET_FLAGS) $(LIBMUSKET_LIBS) $(BUILD_DIR)/*.o -o $(BUILD_DIR)/libmusket.so
	$(CP) $(BUILD_DIR)/libmusket.so ../../../lib

clean:
	$(RMR) $(BUILD_DIR)
	$(MAKE) clean -C $(MUSKET_DIR)
