#!/bin/bash


FILE=$1
TMP=$2
OUT=$3

OUT_WIDTH=512
OUT_HEIGHT=512

# Get the size of input image:
eval $(ffprobe -v error -of flat=s=_ -select_streams v:0 -show_entries stream=height,width ${FILE})
IN_WIDTH=${streams_stream_0_width}
IN_HEIGHT=${streams_stream_0_height}

# Get the difference between actual and desired size
W_DIFF=$[ ${OUT_WIDTH} - ${IN_WIDTH} ]
H_DIFF=$[ ${OUT_HEIGHT} - ${IN_HEIGHT} ]

# Let's take the shorter side, so the image will be at least as big as the desired size:
CROP_SIDE="n"
if [ ${W_DIFF} -lt ${H_DIFF} ] ; then
  SCALE="-2:${OUT_HEIGHT}"
  CROP_SIDE="w"
else
  SCALE="${OUT_WIDTH}:-2"
  CROP_SIDE="h"
fi

# Then perform a first resizing
ffmpeg -i ${FILE} -vf scale=${SCALE} ${TMP}

# Now get the temporary image size
eval $(ffprobe -v error -of flat=s=_ -select_streams v:0 -show_entries stream=height,width ${TMP})
IN_WIDTH=${streams_stream_0_width}
IN_HEIGHT=${streams_stream_0_height}

# Calculate how much we should crop
if [ "z${CROP_SIDE}" = "zh" ] ; then
  DIFF=$[ ${IN_HEIGHT} - ${OUT_HEIGHT} ]
  CROP="in_w:in_h-${DIFF}"
elif [ "z${CROP_SIDE}" = "zw" ] ; then
  DIFF=$[ ${IN_WIDTH} - ${OUT_WIDTH} ]
  CROP="in_w-${DIFF}:in_h"
fi

# Then crop...
ffmpeg -i ${TMP} -filter:v "crop=${CROP}" ${OUT}

rm ${FILE} ${TMP}
