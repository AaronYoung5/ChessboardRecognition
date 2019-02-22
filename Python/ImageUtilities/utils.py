import numpy as np
import cv2 as cv


def calibrate(img):
    kernel = np.ones((5, 5), np.uint8)
    for i in range(5):
        img = cv.GaussianBlur(img, (3, 3), 1)
        img = cv.dilate(img, kernel, iterations=2)
        img = cv.erode(img, kernel, iterations=2)
    img = gray(img)
    img = cv.Canny(img, 127, 128, L2gradient=True)
    img = cv.dilate(img, kernel, iterations=3)
    img = cv.erode(img, kernel, iterations=2)
    return img


def simple_calibrate(img):
    for i in range(3):
        img = cv.GaussianBlur(img, (3, 3), 1)
    img = gray(img)
    return img


def find_center(c, value=10):
    moment = cv.moments(c)
    c_x = int(moment['m10'] / moment['m00'])
    c_y = int(moment['m01'] / moment['m00'])

    return c_x - value, c_y + value


def create_blank(width=700, height=700, black=(0, 0, 0)):
    img = np.zeros((height, width, 3), np.uint8)

    black = tuple(reversed(black))

    img[:] = black

    return img


def get_contour_image(img, approx):
    x, y, w, h = cv.boundingRect(approx)
    roi = img[y:y + h, x:x + w]
    return roi


def color(img):
    return cv.cvtColor(img, cv.COLOR_GRAY2BGR)


def gray(img):
    return cv.cvtColor(img, cv.COLOR_BGR2GRAY)


def reset_text_files():
    writer = open('/Users/aaronyoung/Coding/Chess6/src/PythonCommunicator/initBoard.txt', 'r')
    writer2 = open('/Users/aaronyoung/Coding/Chess6/src/PythonCommunicator/board.txt', 'w')
    writer2.write(writer.read())
