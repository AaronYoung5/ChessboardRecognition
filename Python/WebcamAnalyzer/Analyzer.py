import cv2 as cv
import numpy as np
import sklearn.cluster as sk

from ImageUtilities import utils


# import time


def motion_detected(last_frame, this_frame):
    if are_images_different(last_frame, this_frame):
        return True
    return False


def are_images_different(img1, img2, area_threshold=100):
    img1 = utils.simple_calibrate(img1)
    img2 = utils.simple_calibrate(img2)
    frame_delta = cv.absdiff(img1, img2)
    _, thresh = cv.threshold(frame_delta, 25, 255, cv.THRESH_BINARY)
    thresh = cv.dilate(thresh, None, iterations=2)
    _, contours, _ = cv.findContours(thresh, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)
    for c in contours:
        hull = cv.convexHull(c)
        if cv.contourArea(hull) < area_threshold:
            continue
        return True
    return False


def is_occupied(img, p_thresh=.75, c_thresh=55):
    # cv.imshow('Tester', img)
    img = img.reshape((img.shape[0] * img.shape[1], 3))
    cluster = sk.KMeans(2)
    cluster.fit(img)

    num_labels = np.arange(0, len(np.unique(cluster.labels_)) + 1)
    (hist, _) = np.histogram(cluster.labels_, bins=num_labels)
    hist = hist.astype("float")
    hist /= hist.sum()
    # bar = np.zeros((50, 300, 3), dtype='uint8')
    # start_x = 0
    c1, c2 = [], []
    p1, p2 = 0, 0
    for (percent, color) in zip(hist, cluster.cluster_centers_):
        # end_x = start_x + (percent * 300)
        # cv.rectangle(bar, (int(start_x), 0), (int(end_x), 50), color.astype('uint8').tolist(), -1)
        # start_x = end_x
        if len(c1) == 0:
            c1 = color
            p1 = percent
        else:
            c2 = color
            p2 = percent
    percent_diff = abs(p1 - p2)
    # print('p_thresh : ' + str(p_thresh))
    # print('c_thresh : ' + str(c_thresh))
    # print('c1 : ' + str(c1))
    # print('c2 : ' + str(c2))
    # print('percent_diff : ' + str(percent_diff))
    # print('compare_colors : ' + str((abs(c1[0] - c2[0]) + abs(c1[1] - c2[1]) + abs(c1[2] - c2[2]))))
    # cv.waitKey(0)
    if compare_colors(c1, c2, percent_diff, p_thresh, c_thresh):
        return True
    return False


def compare_colors(c1, c2, percent_diff, p_thresh, c_thresh):
    return percent_diff < p_thresh and (abs(c1[0] - c2[0]) + abs(c1[1] - c2[1]) + abs(c1[2] - c2[2])) > c_thresh


def find_contours(blank_img):
    squares = []
    _, contours, _ = cv.findContours(blank_img, cv.RETR_CCOMP, cv.CHAIN_APPROX_NONE)
    if len(contours) == 0:
        return
    for c in contours:
        hull = cv.convexHull(c)
        epsilon = 0.1 * cv.arcLength(hull, True)
        approx = cv.approxPolyDP(hull, epsilon, True)
        if 100000 > cv.contourArea(approx) > 2000 and len(approx) == 4:
            squares.append(approx)
    return squares


def determine_player_color():
    return True


def find_corners(img):
    img = cv.cvtColor(img, cv.COLOR_BGR2RGB)

    cv.imshow('original', img)
    boundaries = [
        ([80, 100, 130], [150, 175, 230]),
        ([0, 0, 0], [0, 255, 255]),
        ([25, 146, 190], [62, 174, 250]),
        ([103, 86, 65], [145, 133, 128])
    ]

    for (lower, upper) in boundaries:
        # create NumPy arrays from the boundaries
        lower = np.array(lower, dtype="uint8")
        upper = np.array(upper, dtype="uint8")

        # find the colors within the specified boundaries and apply
        # the mask
        mask = cv.inRange(img, lower, upper)
        output = cv.bitwise_and(img, img, mask=mask)

        # show the images
        cv.imshow("images", output)
        print('Test')
        cv.waitKey(0)
