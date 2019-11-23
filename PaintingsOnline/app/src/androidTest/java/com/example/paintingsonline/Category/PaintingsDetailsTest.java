package com.example.paintingsonline.Category;

import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PaintingsDetailsTest
{

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    @UiThreadTest
    public void testVerify()
    {
        // create and configure mock
        PaintingsDetails test = Mockito.mock(PaintingsDetails.class);

        verify(test, atLeastOnce()).AddToCart();

    }

}